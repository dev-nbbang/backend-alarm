package com.dev.nbbang.alarm.global.exception;

public enum NbbangException {
    NOT_FOUND_EVENT("BE001", "Doesn't Find Event"),
    NOT_CREATE_EVENT("BE002", "Doesn't Create Event"),
    FAIL_DELETE_IMAGES("BE003", "Fail To Delete Images"),
    NOT_CREATE_NOTICE("BE101", "Doesn't Create Notice"),
    NOT_FOUND_NOTICE("BE102", "Doesn't Find Notice"),
    NOT_CREATE_IMAGE("BE400", "Doesn't Create Image"),
    ACCESS_DENIED("BE401","Access Denied"),;



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
