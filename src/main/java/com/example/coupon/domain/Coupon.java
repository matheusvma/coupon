package com.example.coupon.domain;

import com.example.coupon.enumerator.CouponStatus;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.Type;

@Entity
public class Coupon {
    private static final double MIN_DISCOUNT_VALUE = 0.5d;
    private static final int CODE_LENGTH = 6;

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "code", nullable = false, length = CODE_LENGTH)
    private String code;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "discount_value", nullable = false)
    private Double discountValue;

    @Column(name = "expiration_date", nullable = false)
    private OffsetDateTime expirationDate;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "published", nullable = false)
    private Boolean published;

    @Column(name = "redeemed", nullable = false)
    private Boolean redeemed;

    public static Coupon create(
            String rawCode,
            String description,
            Double discountValue,
            OffsetDateTime expirationDate,
            Boolean published,
            OffsetDateTime now
    ) {

        Coupon c = new Coupon();
        c.id = UUID.randomUUID();
        c.setCodeInternal(rawCode);
        c.setDescriptionInternal(description);
        c.setDiscountValueInternal(discountValue);
        c.setExpirationDateInternal(expirationDate, now);
        c.published = published != null ? published : false;
        c.redeemed = false;
        c.status = CouponStatus.ACTIVE.getCode();

        return c;
    }

    public void softDelete(OffsetDateTime now) {
        if (now == null) throw new IllegalArgumentException("Data é obrigatoria!");
        if (isDeleted()) return;
        this.deletedAt = now;
        this.status = CouponStatus.DELETED.getCode();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    private void setCodeInternal(String rawCode) {
        String normalized = normalizeCouponCode(rawCode);
        if (normalized.length() != CODE_LENGTH) {
            throw new IllegalArgumentException(
                    "O código deve ser alfanumérico e ter exatamente 6 caracteres após a normalização."
            );
        }
        this.code = normalized;
    }

    private static String normalizeCouponCode(String raw) {
        if (raw == null) return "";
        String onlyAlnum = raw.replaceAll("[^A-Za-z0-9]", "");
        return onlyAlnum.toUpperCase();
    }

    private void setDescriptionInternal(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description é obirgatório");
        }
        this.description = description;
    }

    private void setDiscountValueInternal(Double discountValue) {
        if (discountValue == null) {
            throw new IllegalArgumentException("DiscountValue é obirgatório");
        }
        if (discountValue < MIN_DISCOUNT_VALUE) {
            throw new IllegalArgumentException("DiscountValue tem que ser no minimo 0.5");
        }
        this.discountValue = discountValue;
    }

    private void setExpirationDateInternal(OffsetDateTime expirationDate, OffsetDateTime now) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("ExpirationDate é obirgatório");
        }
        if (now == null) {
            throw new IllegalArgumentException("Data é obirgatória");
        }
        if (expirationDate.isBefore(now)) {
            throw new IllegalArgumentException("ExpirationDate não pode estar em uma data passada.");
        }
        this.expirationDate = expirationDate;
    }

    public UUID getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public Double getDiscountValue() { return discountValue; }
    public OffsetDateTime getExpirationDate() { return expirationDate; }
    public OffsetDateTime getDeletedAt() { return deletedAt; }
    public String getStatus() { return status; }
    public Boolean getPublished() { return published; }
    public Boolean getRedeemed() { return redeemed; }
}