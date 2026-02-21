package com.example.coupon.domain;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
public class Coupon {

	@Id
    @Column(nullable = false, updatable = false)
	private UUID id;

    @PrePersist
    private void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }

	@Column(name = "code", nullable = false, length = 6)
	private Long code;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public OffsetDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(OffsetDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(Boolean redeemed) {
        this.redeemed = redeemed;
    }

    public void softDelete() {
        this.deletedAt = OffsetDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
