package com.pinkcat.quickreservemvp.common.enums;

public enum HotDealPublicStatusEnum {
    ADMIN_ONLY("관리자"),
    ADMIN_SELLER("관리자 및 판매자"),
    ALL("전체");

    private final String value;

    HotDealPublicStatusEnum(String value) {
        this.value = value;
    }
}
