package com.dev.nbbang.alarm.global.exception;

public enum NbbangException {
    NOT_FOUND_EVENT("BE001", "Doesn't Find Event"),
    NOT_CREATE_EVENT("BE002", "Doesn't Create Event"),
    FAIL_DELETE_IMAGES("BE003", "Fail To Delete Images");



    private String code;
    private String message;

    NbbangException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
