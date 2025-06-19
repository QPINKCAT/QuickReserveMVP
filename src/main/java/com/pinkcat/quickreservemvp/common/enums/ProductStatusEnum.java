package com.pinkcat.quickreservemvp.common.enums;

public enum ProductStatusEnum {
    ON("판매중"),
    OFF("미판매");

    private final String value;;
    ProductStatusEnum(String value){
        this.value = value;
    }
}
