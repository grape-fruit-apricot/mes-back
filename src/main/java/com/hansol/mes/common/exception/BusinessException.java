package com.hansol.mes.common.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public BusinessException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public static <T> T requireFound(T value, String name) {
        if (value == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "NOT_FOUND", name + "을(를) 찾을 수 없습니다.");
        }
        return value;
    }
}
