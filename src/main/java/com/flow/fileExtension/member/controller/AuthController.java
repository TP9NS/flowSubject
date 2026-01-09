package com.flow.fileExtension.member.controller;

import com.flow.fileExtension.config.security.JwtProvider;
import com.flow.fileExtension.member.controller.dto.LoginRequest;
import com.flow.fileExtension.member.controller.dto.SignupRequest;
import com.flow.fileExtension.member.entity.Member;
import com.flow.fileExtension.member.entity.Role;
import com.flow.fileExtension.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest loginRequest,
                        BindingResult br,
                        HttpServletResponse response,
                        Model model) {

        if (br.hasErrors()) return "auth/login";

        var memberOpt = memberRepository.findByEmail(loginRequest.getEmail());
        if (memberOpt.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), memberOpt.get().getPassword())) {
            model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "auth/login";
        }

        Member member = memberOpt.get();
        String role = (member.getRole() == Role.ADMIN) ? "ROLE_ADMIN" : "ROLE_USER";
        String token = jwtProvider.issueToken(member.getId(), member.getEmail(), role);

        Cookie cookie = new Cookie(jwtProvider.getCookieName(), token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtProvider.getExpMinutes() * 60);
        response.addCookie(cookie);

        return "redirect:/";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupRequest signupRequest,
                         BindingResult br,
                         Model model) {

        if (memberRepository.existsByEmail(signupRequest.getEmail())) {
            model.addAttribute("error", "이미 가입된 이메일입니다.");
            return "auth/signup";
        }

        Member member = Member.builder()
                .email(signupRequest.getEmail())
                .name(signupRequest.getName())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(Role.USER)
                .build();

        memberRepository.save(member);
        return "redirect:/auth/login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(jwtProvider.getCookieName(), "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
