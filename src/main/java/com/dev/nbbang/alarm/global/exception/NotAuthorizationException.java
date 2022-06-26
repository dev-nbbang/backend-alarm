package com.dev.nbbang.alarm.global.exception;

import org.springframework.http.HttpStatus;

public class NotAuthorizationException extends NbbangCommonException {
    private final NbbangException nbbangException;
    private final String message;
    public NotAuthorizationException(String message, NbbangException nbbangException) {
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
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
