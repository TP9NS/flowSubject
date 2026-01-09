package com.flow.fileExtension.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;
    private final String cookieName;
    private final int expMinutes;

    public JwtProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.cookieName}") String cookieName,
            @Value("${app.jwt.expMinutes}") int expMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.cookieName = cookieName;
        this.expMinutes = expMinutes;
    }

    public String getCookieName() {
        return cookieName;
    }

    public int getExpMinutes() {
        return expMinutes;
    }

    public String issueToken(Long memberId, String email, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + (long) expMinutes * 60 * 1000);

        return Jwts.builder()
                .subject(email)
                .claim("memberId", memberId)
                .claim("role", role) // "ROLE_ADMIN" / "ROLE_USER"
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    /** 토큰 유효성 검증 (서명/만료/형식) */
    public boolean validate(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** 토큰에서 email(subject) 추출 */
    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    /** 필요하면 memberId도 꺼낼 수 있게 */
    public Long getMemberId(String token) {
        return parseClaims(token).get("memberId", Number.class).longValue();
    }

    /** 필요하면 role도 꺼낼 수 있게 */
    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public JwtPayload verify(String token) {
        Claims claims = parseClaims(token);

        Long memberId = claims.get("memberId", Number.class).longValue();
        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        return new JwtPayload(memberId, email, role);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
