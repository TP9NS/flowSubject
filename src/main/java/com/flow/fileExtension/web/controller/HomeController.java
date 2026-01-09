package com.flow.fileExtension.web.controller;

import com.flow.fileExtension.member.entity.Member;
import com.flow.fileExtension.member.repository.MemberRepository;
import com.flow.fileExtension.config.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {

        String token = extractTokenFromCookie(request, jwtProvider.getCookieName());

        if (token == null || !jwtProvider.validate(token)) {
            model.addAttribute("loggedIn", false);
            return "home";
        }

        String email = jwtProvider.getEmail(token);
        model.addAttribute("loggedIn", true);
        model.addAttribute("email", email);

        return "home";
    }

    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
