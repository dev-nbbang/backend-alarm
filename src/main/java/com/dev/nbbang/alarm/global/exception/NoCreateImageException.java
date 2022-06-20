package com.dev.nbbang.alarm.global.exception;

import org.springframework.http.HttpStatus;

public class NoCreateImageException extends NbbangCommonException {
    private final NbbangException nbbangException;
    private final String message;
    public NoCreateImageException(String message, NbbangException nbbangException) {
        super(message);
        this.message = message;
        this.nbbangException = nbbangException;
    }

    @Override
    public String getErrorCode() {
        return nbbangException.getCode();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
