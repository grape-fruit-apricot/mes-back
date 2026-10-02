package com.hansol.mes.common.exception;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public record ErrorResponse(String code, String message) {}

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> business(BusinessException e) {
        return response(e.getStatus(), e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> validation(BindException e) {
        String message = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("입력값을 확인해 주세요.");
        return response(HttpStatus.BAD_REQUEST, "INVALID_INPUT", message);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            HandlerMethodValidationException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ErrorResponse> invalid(Exception e) {
        return response(
                HttpStatus.BAD_REQUEST,
                "INVALID_INPUT",
                "입력 형식과 수량을 확인해 주세요. 수량은 정수여야 합니다."
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> database(DataAccessException e) {
        for (Throwable cause = e; cause != null; cause = cause.getCause()) {
            if (cause instanceof SQLException sql) {
                switch (sql.getErrorCode()) {
                    case 1403:
                        return response(
                                HttpStatus.NOT_FOUND,
                                "NOT_FOUND",
                                "대상 데이터를 찾을 수 없습니다."
                        );
                    case 20001, 20002:
                        return response(
                                HttpStatus.CONFLICT,
                                "INVALID_WORK_STATUS",
                                "작업지시가 없거나 현재 상태에서 처리할 수 없습니다."
                        );
                    case 20003:
                        return response(
                                HttpStatus.BAD_REQUEST,
                                "PRODUCTION_QUANTITY_MISMATCH",
                                "양품 수량과 불량 수량의 합계는 지시 수량과 같아야 합니다."
                        );
                    case 20004:
                        return response(
                                HttpStatus.BAD_REQUEST,
                                "DEFECT_REASON_REQUIRED",
                                "불량 수량이 있으면 불량 사유를 입력해 주세요."
                        );
                    case 20005:
                        return response(
                                HttpStatus.CONFLICT,
                                "ALREADY_RECEIVED",
                                "이미 입고된 생산실적입니다."
                        );
                    case 20006:
                        return response(
                                HttpStatus.CONFLICT,
                                "NO_GOOD_QUANTITY",
                                "양품이 없어 입고할 수 없습니다."
                        );
                    case 1:
                        return response(
                                HttpStatus.CONFLICT,
                                "DUPLICATE_DATA",
                                "이미 등록된 데이터입니다."
                        );
                    case 2291:
                        return response(
                                HttpStatus.BAD_REQUEST,
                                "INVALID_REFERENCE",
                                "참조하는 데이터가 존재하지 않습니다."
                        );
                    case 54, 60, 30006:
                        return response(
                                HttpStatus.CONFLICT,
                                "RESOURCE_BUSY",
                                "다른 요청이 처리 중입니다. 잠시 후 다시 시도해 주세요."
                        );
                    default:
                        break;
                }
            }
        }
        log.error("Database operation failed", e);
        return response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "DATABASE_ERROR",
                "데이터 처리에 실패했습니다."
        );
    }

    private ResponseEntity<ErrorResponse> response(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(code, message));
    }
}
