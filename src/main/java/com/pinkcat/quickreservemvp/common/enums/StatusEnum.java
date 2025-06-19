package com.pinkcat.quickreservemvp.common.enums;

public enum StatusEnum {

    PENDING("주문 대기"),
    COMPLETED("주문 완료"),
    CANCELLED("주문 취소"),
    USED("사용 완료");

    private final String value;

    StatusEnum(String value) {
        this.value = value;
    }
}
