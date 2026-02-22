package com.example.coupon.domain.dto;

import com.example.coupon.domain.Coupon;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CouponResponseDto {
    private final UUID id;
    private final String code;
    private final String description;
    private final Double discountValue;
    private final OffsetDateTime expirationDate;
    private final String status;
    private final Boolean published;
    private final Boolean redeemed;

    public CouponResponseDto(
            UUID id,
            String code,
            String description,
            Double discountValue,
            OffsetDateTime expirationDate,
            String status,
            Boolean published,
            Boolean redeemed
    ) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = status;
        this.published = published;
        this.redeemed = redeemed;
    }

    public UUID getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public Double getDiscountValue() { return discountValue; }
    public OffsetDateTime getExpirationDate() { return expirationDate; }
    public String getStatus() { return status; }
    public Boolean getPublished() { return published; }
    public Boolean getRedeemed() { return redeemed; }

    public static CouponResponseDto toResponseDto(Coupon c) {
        return new CouponResponseDto(
                c.getId(),
                c.getCode(),
                c.getDescription(),
                c.getDiscountValue(),
                c.getExpirationDate(),
                c.getStatus(),
                c.getPublished(),
                c.getRedeemed()
        );
    }
}
