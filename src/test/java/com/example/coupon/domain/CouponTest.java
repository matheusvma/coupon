package com.example.coupon.domain;

import com.example.coupon.enumerator.CouponStatus;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    private static final OffsetDateTime NOW = OffsetDateTime.parse("2026-02-22T10:00:00Z");

    @Test
    void create_whenValid_createsCouponWithDefaultsAndNormalizedCode() {
        Coupon c = Coupon.create(
                "ab-c 12 3",            // vira ABC123
                "Cupom de desconto",
                10.0,
                NOW.plusDays(1),
                null,                   // default false
                NOW
        );

        assertNotNull(c.getId(), "id deve ser gerado");
        assertEquals("ABC123", c.getCode(), "code deve ser normalizado: apenas alfanumérico e uppercase");
        assertEquals("Cupom de desconto", c.getDescription());
        assertEquals(10.0, c.getDiscountValue());
        assertEquals(NOW.plusDays(1), c.getExpirationDate());

        assertFalse(c.getPublished(), "published deve ser false quando null");
        assertFalse(c.getRedeemed(), "redeemed deve iniciar false");
        assertEquals(CouponStatus.ACTIVE.getCode(), c.getStatus(), "status inicial deve ser ACTIVE");
        assertFalse(c.isDeleted());
        assertNull(c.getDeletedAt());
    }

    @Test
    void create_whenPublishedProvided_setsPublished() {
        Coupon c = Coupon.create(
                "ABC123",
                "Cupom",
                10.0,
                NOW.plusDays(1),
                true,
                NOW
        );

        assertTrue(c.getPublished());
    }

    @Test
    void create_whenRawCodeNull_throwsInvalidCodeLength() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                Coupon.create(
                        null,
                        "Cupom",
                        10.0,
                        NOW.plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals(
                "O código deve ser alfanumérico e ter exatamente 6 caracteres após a normalização.",
                ex.getMessage()
        );
    }

    @Test
    void create_whenRawCodeNormalizesToLessThan6_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                Coupon.create(
                        "A-1", // normaliza para "A1" (2 chars)
                        "Cupom",
                        10.0,
                        NOW.plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals(
                "O código deve ser alfanumérico e ter exatamente 6 caracteres após a normalização.",
                ex.getMessage()
        );
    }

    @Test
    void create_whenRawCodeNormalizesToMoreThan6_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                Coupon.create(
                        "ABC1234", // 7 chars
                        "Cupom",
                        10.0,
                        NOW.plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals(
                "O código deve ser alfanumérico e ter exatamente 6 caracteres após a normalização.",
                ex.getMessage()
        );
    }

    @Test
    void create_whenDescriptionNull_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                Coupon.create(
                        "ABC123",
                        null,
                        10.0,
                        NOW.plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals("Description é obirgatório", ex.getMessage());
    }

    @Test
    void create_whenDescriptionBlank_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                Coupon.create(
                        "ABC123",
                        "   ",
                        10.0,
                        NOW.plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals("Description é obirgatório", ex.getMessage());
    }

    @Test
    void create_whenDiscountValueNull_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                Coupon.create(
                        "ABC123",
                        "Cupom",
                        null,
                        NOW.plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals("DiscountValue é obirgatório", ex.getMessage());
    }

    @Test
    void create_whenDiscountValueLessThanMin_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                Coupon.create(
                        "ABC123",
                        "Cupom",
                        0.49,
                        NOW.plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals("DiscountValue tem que ser no minimo 0.5", ex.getMessage());
    }

    @Test
    void create_whenDiscountValueEqualsMin_isAllowed() {
        Coupon c = Coupon.create(
                "ABC123",
                "Cupom",
                0.5,
                NOW.plusDays(1),
                false,
                NOW
        );

        assertEquals(0.5, c.getDiscountValue());
    }

    @Test
    void create_whenExpirationDateNull_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                Coupon.create(
                        "ABC123",
                        "Cupom",
                        10.0,
                        null,
                        false,
                        NOW
                )
        );

        assertEquals("ExpirationDate é obirgatório", ex.getMessage());
    }

    @Test
    void create_whenNowNull_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                Coupon.create(
                        "ABC123",
                        "Cupom",
                        10.0,
                        NOW.plusDays(1),
                        false,
                        null
                )
        );

        assertEquals("Data é obirgatória", ex.getMessage());
    }

    @Test
    void create_whenExpirationDateInPast_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                Coupon.create(
                        "ABC123",
                        "Cupom",
                        10.0,
                        NOW.minusSeconds(1),
                        false,
                        NOW
                )
        );

        assertEquals("ExpirationDate não pode estar em uma data passada.", ex.getMessage());
    }

    @Test
    void softDelete_whenNowNull_throws() {
        Coupon c = Coupon.create(
                "ABC123",
                "Cupom",
                10.0,
                NOW.plusDays(1),
                false,
                NOW
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> c.softDelete(null));
        assertEquals("Data é obrigatoria!", ex.getMessage());
    }

    @Test
    void softDelete_whenNotDeleted_setsDeletedAtAndStatus() {
        Coupon c = Coupon.create(
                "ABC123",
                "Cupom",
                10.0,
                NOW.plusDays(1),
                false,
                NOW
        );

        OffsetDateTime deletedAt = NOW.plusHours(2);
        c.softDelete(deletedAt);

        assertTrue(c.isDeleted());
        assertEquals(deletedAt, c.getDeletedAt());
        assertEquals(CouponStatus.DELETED.getCode(), c.getStatus());
    }

    @Test
    void softDelete_whenAlreadyDeleted_isIdempotentAndKeepsOriginalDeletedAt() {
        Coupon c = Coupon.create(
                "ABC123",
                "Cupom",
                10.0,
                NOW.plusDays(1),
                false,
                NOW
        );

        OffsetDateTime firstDelete = NOW.plusHours(1);
        OffsetDateTime secondDelete = NOW.plusHours(3);

        c.softDelete(firstDelete);
        c.softDelete(secondDelete); // deve retornar sem alterar

        assertEquals(firstDelete, c.getDeletedAt(), "não deve alterar deletedAt se já deletado");
        assertEquals(CouponStatus.DELETED.getCode(), c.getStatus());
    }
}