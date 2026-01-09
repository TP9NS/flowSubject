package com.flow.fileExtension.member.controller;

import com.flow.fileExtension.global.dto.ResponseDto;
import com.flow.fileExtension.member.controller.dto.LoginRequest;
import com.flow.fileExtension.member.controller.dto.SignupRequest;
import com.flow.fileExtension.member.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseDto<Void> signup(@Valid @RequestBody SignupRequest req) {
        authService.signup(req);
        return ResponseDto.ok();
    }

    @PostMapping("/login")
    public ResponseDto<Void> login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        authService.login(req, response);
        return ResponseDto.ok();
    }

    @PostMapping("/logout")
    public ResponseDto<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseDto.ok();
    }
}
