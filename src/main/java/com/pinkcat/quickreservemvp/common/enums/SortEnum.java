package com.pinkcat.quickreservemvp.common.enums;

public enum SortEnum {
    ASC("오름차순"),
    DESC("내림차순");

    private final String value;

    SortEnum(String value){
        this.value = value;
    }
}
