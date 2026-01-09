package com.flow.fileExtension.member.service;

import com.flow.fileExtension.config.security.JwtProvider;
import com.flow.fileExtension.global.exception.BusinessException;
import com.flow.fileExtension.global.exception.ErrorCode;
import com.flow.fileExtension.member.controller.dto.LoginRequest;
import com.flow.fileExtension.member.controller.dto.SignupRequest;
import com.flow.fileExtension.member.entity.Member;
import com.flow.fileExtension.member.entity.Role;
import com.flow.fileExtension.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void signup(SignupRequest req) {
        if (memberRepository.existsByEmail(req.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL, "파일 저장 실패");
        }

        Member member = Member.builder()
                .email(req.getEmail())
                .name(req.getName())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.USER)
                .build();

        memberRepository.save(member);
    }

    public void login(LoginRequest req, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED, "파일 저장 실패"));

        if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED, "파일 저장 실패");
        }

        String role = (member.getRole() == Role.ADMIN) ? "ROLE_ADMIN" : "ROLE_USER";
        String token = jwtProvider.issueToken(member.getId(), member.getEmail(), role);

        Cookie cookie = new Cookie(jwtProvider.getCookieName(), token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtProvider.getExpMinutes() * 60);
        response.addCookie(cookie);
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(jwtProvider.getCookieName(), "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
