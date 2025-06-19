package com.pinkcat.quickreservemvp.common.enums;

public enum PaymentStatusEnum {

    CANCELLED("결제취소"),
    COMPLETED("결제완료");
    private final String value;

    PaymentStatusEnum(String value){
        this.value = value;
    }
}
