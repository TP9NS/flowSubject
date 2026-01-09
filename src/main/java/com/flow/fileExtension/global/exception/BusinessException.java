package com.flow.fileExtension.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String 파일_저장_실패) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
