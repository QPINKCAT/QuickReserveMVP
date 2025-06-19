package com.pinkcat.quickreservemvp.common.enums;

public enum OrderStatusEnum {

    PENDING("주문대기"),
    COMPLETED("주문완료"),
    CANCELLED("주문취소"),
    USED("사용완료");

    private final String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }
}
