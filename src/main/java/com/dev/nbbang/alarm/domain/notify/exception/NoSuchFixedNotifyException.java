package com.dev.nbbang.alarm.domain.notify.exception;

import com.dev.nbbang.alarm.global.exception.NbbangCommonException;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import org.springframework.http.HttpStatus;

public class NoSuchFixedNotifyException extends NbbangCommonException {
    private final NbbangException nbbangException;
    private final String message;
    public NoSuchFixedNotifyException(String message, NbbangException nbbangException) {
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
