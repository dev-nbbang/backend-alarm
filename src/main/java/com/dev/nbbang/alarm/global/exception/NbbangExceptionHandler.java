package com.dev.nbbang.alarm.global.exception;

import com.dev.nbbang.alarm.global.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class NbbangExceptionHandler {
    @ExceptionHandler(GrantAccessDeniedException.class)
    public ResponseEntity<CommonResponse> handleGrantException(GrantAccessDeniedException e) {
        log.warn("Nbbang Exception Code : " + e.getErrorCode());
        log.warn("Nbbang Exception message : " + e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(CommonResponse.response(false, e.getMessage()));
    }

    @ExceptionHandler(NbbangCommonException.class)
    public ResponseEntity<CommonResponse> handleBaseException(NbbangCommonException e) {
        log.warn("Nbbang Exception Code : " + e.getErrorCode());
        log.warn("Nbbang Exception message : " + e.getMessage());

        return ResponseEntity.ok(CommonResponse.response(false, e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgument Exception");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse.response(false, "Bad Request"));
    }

    @ExceptionHandler(NotAuthorizationException.class)
    public ResponseEntity<CommonResponse> handleNotAuthorzationException(NotAuthorizationException e) {
        log.warn("Nbbang Exception Code : " + e.getErrorCode());
        log.warn("Nbbang Exception message : " + e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(CommonResponse.response(false, e.getMessage()));
    }
}
