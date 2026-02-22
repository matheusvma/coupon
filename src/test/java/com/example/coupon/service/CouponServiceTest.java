package com.example.coupon.service;

import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.dto.CouponResponseDto;
import com.example.coupon.domain.dto.CreateCouponRequest;
import com.example.coupon.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository repository;

    private CouponService service;

    @BeforeEach
    void setUp() {
        service = new CouponService(repository);
    }

    @Test
    void findById_whenExists_returnsDto() {
        UUID id = UUID.randomUUID();
        Coupon coupon = mock(Coupon.class);

        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        CouponResponseDto dto = service.findById(id);

        assertNotNull(dto);
        verify(repository).findById(id);
    }

    @Test
    void findById_whenMissing_throws404() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.findById(id));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Coupon não encontrado", ex.getReason());
    }

    @Test
    void save_whenValid_persistsAndReturnsDto() {
        CreateCouponRequest req = mock(CreateCouponRequest.class);
        when(req.getCode()).thenReturn("ABC123");
        when(req.getDescription()).thenReturn("Cupom");
        when(req.getDiscountValue()).thenReturn(10.0);
        when(req.getExpirationDate()).thenReturn(OffsetDateTime.now().plusDays(1));
        when(req.getPublished()).thenReturn(Boolean.TRUE);

        Coupon createdCoupon = mock(Coupon.class);
        Coupon savedCoupon = mock(Coupon.class);

        // mock do método estático Coupon.create(...)
        try (MockedStatic<Coupon> mocked = mockStatic(Coupon.class)) {
            mocked.when(() -> Coupon.create(anyString(), anyString(), anyDouble(), any(OffsetDateTime.class),
                    any(Boolean.class), any(OffsetDateTime.class))).thenReturn(createdCoupon);

            when(repository.save(createdCoupon)).thenReturn(savedCoupon);

            CouponResponseDto dto = service.save(req);

            assertNotNull(dto);

            verify(repository).save(createdCoupon);

            // garante que chamou Coupon.create(...) (com now)
            mocked.verify(() -> Coupon.create(eq("ABC123"), eq("Cupom"), eq(10.0), any(OffsetDateTime.class), eq(Boolean.TRUE), any(OffsetDateTime.class)));
        }
    }

    @Test
    void save_whenDomainThrowsIllegalArgument_throws400() {
        CreateCouponRequest req = mock(CreateCouponRequest.class);
        when(req.getCode()).thenReturn("BAD");
        when(req.getDescription()).thenReturn("Cupom");
        when(req.getDiscountValue()).thenReturn(0.1);
        when(req.getExpirationDate()).thenReturn(OffsetDateTime.now().minusDays(1));
        when(req.getPublished()).thenReturn(Boolean.FALSE);

        try (MockedStatic<Coupon> mocked = mockStatic(Coupon.class)) {
            mocked.when(() -> Coupon.create(any(), any(), any(), any(), any(), any())).thenThrow(new IllegalArgumentException("invalid"));

            ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.save(req));

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
            assertEquals("invalid", ex.getReason());
            verify(repository, never()).save(any());
        }
    }

    @Test
    void delete_whenExistsAndNotDeleted_softDeletesAndSaves() {
        UUID id = UUID.randomUUID();
        Coupon coupon = mock(Coupon.class);

        when(repository.findById(id)).thenReturn(Optional.of(coupon));
        when(coupon.isDeleted()).thenReturn(false);
        when(repository.save(coupon)).thenReturn(coupon);

        CouponResponseDto dto = service.delete(id);

        assertNotNull(dto);

        verify(coupon).softDelete(any(OffsetDateTime.class));
        verify(repository).save(coupon);
    }

    @Test
    void delete_whenMissing_throws404() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.delete(id));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void delete_whenAlreadyDeleted_throws406() {
        UUID id = UUID.randomUUID();
        Coupon coupon = mock(Coupon.class);

        when(repository.findById(id)).thenReturn(Optional.of(coupon));
        when(coupon.isDeleted()).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.delete(id));

        assertEquals(HttpStatus.NOT_ACCEPTABLE, ex.getStatus());
        verify(repository, never()).save(any());
    }
}