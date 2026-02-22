package com.example.coupon.enumerator;

public enum CouponStatus {
    ACTIVE("ACTIVE", "Ativo"),
    INACTIVE("INACTIVE", "Inativo"),
    DELETED("DELETED", "Deletado");

    private final String code;
    private final String displayName;

    CouponStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }
}
