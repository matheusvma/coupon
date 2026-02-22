package com.example.coupon.controller;

import com.example.coupon.domain.dto.CouponResponseDto;
import com.example.coupon.service.CouponService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService service;

    @Test
    void getById_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        CouponResponseDto dto = new CouponResponseDto(
                id,
                "ABC123",
                "Cupom",
                10.0,
                OffsetDateTime.parse("2026-12-20T00:00:00Z"),
                "ACTIVE",
                false,
                false
        );

        when(service.findById(id)).thenReturn(dto);

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isOk());

        verify(service).findById(id);
    }

    @Test
    void getById_whenServiceThrows404_returns404() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.findById(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon não encontrado"));

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void postSave_returns201() throws Exception {
        CouponResponseDto dto = new CouponResponseDto(
                UUID.randomUUID(),
                "ABC123",
                "Cupom",
                10.0,
                OffsetDateTime.parse("2026-12-20T00:00:00Z"),
                "ACTIVE",
                false,
                false
        );
        when(service.save(any())).thenReturn(dto);

        // JSON mínimo (ajuste campos conforme seu CreateCouponRequest real)
        String body = """
                {
                  "code": "ABC123",
                  "description": "Cupom",
                  "discountValue": 10.0,
                  "expirationDate": "2026-12-20T00:00:00Z",
                  "published": true
                }
                """;

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        verify(service).save(any());
    }

    @Test
    void delete_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        CouponResponseDto dto = new CouponResponseDto(
                id,
                "ABC123",
                "Cupom",
                10.0,
                OffsetDateTime.parse("2026-12-20T00:00:00Z"),
                "ACTIVE",
                false,
                false
        );

        when(service.delete(id)).thenReturn(dto);

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isOk());

        verify(service).delete(id);
    }
}
