package com.pinkcat.quickreservemvp.common.model;

import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;

import java.time.Instant;

public class BaseResponse<T> {
    long timestamp = Instant.now().toEpochMilli();
    int status = 200;
    String error = "";
    String message = "";
    String exception = "";
    T data = null;

    public BaseResponse(String message, PinkCatException customException ) {
        customException.printStackTrace();
        this.message = message;
        this.status = customException.getErrorMessageCode().getCode();
        this.error = customException.getErrors().toString();
        this.exception = customException.getErrorMessageCode().name();
    }

    public BaseResponse(PinkCatException customException ) {
        customException.printStackTrace();
        this.message = customException.getMessage();
        this.status = customException.getErrorMessageCode().getCode();
        this.error = customException.getErrors().toString();
        this.exception = customException.getErrorMessageCode().name();
    }
}