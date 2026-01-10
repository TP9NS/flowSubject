package com.flow.fileExtension.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "입력값이 올바르지 않습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 오류가 발생했습니다."),

    // Auth
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "이미 가입된 이메일입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "LOGIN_FAILED", "이메일 또는 비밀번호가 올바르지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다."),

    // Extension
    FIXED_NOT_FOUND(HttpStatus.NOT_FOUND, "FIXED_NOT_FOUND", "고정 확장자를 찾을 수 없습니다."),
    CUSTOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CUSTOM_NOT_FOUND", "커스텀 확장자를 찾을 수 없습니다."),
    CUSTOM_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "CUSTOM_LIMIT_EXCEEDED", "커스텀 확장자는 최대 200개까지 등록할 수 있습니다."),
    CUSTOM_DUPLICATED(HttpStatus.CONFLICT, "CUSTOM_DUPLICATED", "이미 등록된 확장자입니다."),
    EXTENSION_INVALID(HttpStatus.BAD_REQUEST, "EXTENSION_INVALID", "확장자 형식이 올바르지 않습니다."),
    BLOCKED_EXTENSION(HttpStatus.BAD_REQUEST, "BLOCKED_EXTENSION", "허락되지 않은 확장자입니다."),

    //File
    FORBIDDEN(HttpStatus.BAD_REQUEST, "FORBIDDEN", "허락되지 않은 파일입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "파일을 찾지 못했습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
