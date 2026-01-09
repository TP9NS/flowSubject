package com.flow.fileExtension.global.exception;

import com.flow.fileExtension.global.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDto<Void>> handleBusiness(BusinessException e) {
        ErrorCode ec = e.getErrorCode();
        return ResponseEntity.status(ec.getStatus())
                .body(ResponseDto.fail(ec.getCode(), ec.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Void>> handleValidation(MethodArgumentNotValidException e) {
        FieldError fe = e.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String msg = (fe != null && fe.getDefaultMessage() != null)
                ? fe.getDefaultMessage()
                : ErrorCode.INVALID_INPUT.getMessage();

        return ResponseEntity.status(ErrorCode.INVALID_INPUT.getStatus())
                .body(ResponseDto.fail(ErrorCode.INVALID_INPUT.getCode(), msg));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<Void>> handleIllegalArg(IllegalArgumentException e) {
        return ResponseEntity.status(ErrorCode.INVALID_INPUT.getStatus())
                .body(ResponseDto.fail(ErrorCode.INVALID_INPUT.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleEtc(Exception e) {
        // 필요하면 로그
        // e.printStackTrace();
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus())
                .body(ResponseDto.fail(ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getMessage()));
    }
}
