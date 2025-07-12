package com.pinkcat.quickreservemvp.common.enums;

public enum SortPivotEnum {
    CREATED_AT("등록일"),
    START_AT("시작일");

    private final String value;

    SortPivotEnum(String value){
        this.value = value;
    }
}
