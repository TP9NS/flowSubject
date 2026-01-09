package com.flow.fileExtension.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtPayload {
    private Long memberId;
    private String email;
    private String role; // "ROLE_ADMIN" / "ROLE_USER"
}
