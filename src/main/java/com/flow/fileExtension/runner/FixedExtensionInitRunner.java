package com.flow.fileExtension.runner;

import com.flow.fileExtension.extension.entity.FixedExtension;
import com.flow.fileExtension.extension.repository.FixedExtensionRepository;
import com.flow.fileExtension.member.entity.Member;
import com.flow.fileExtension.member.entity.Role;
import com.flow.fileExtension.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FixedExtensionInitRunner implements CommandLineRunner {

    private final FixedExtensionRepository fixedExtensionRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) {
        if (fixedExtensionRepository.count() > 0) return;

        List<String> fixed = List.of(
                "bat", "cmd", "com", "cpl", "exe", "scr", "js"
        );

        for (String ext : fixed) {
            fixedExtensionRepository.save(
                    FixedExtension.builder()
                            .extension(ext)
                            .blocked(false)
                            .build()
            );
        }
        String adminEmail = "admin@admin.com";
        if (!memberRepository.existsByEmail(adminEmail)) {
            memberRepository.save(
                    Member.builder()
                            .email(adminEmail)
                            .name("admin")
                            .password(passwordEncoder.encode("admin123"))
                            .role(Role.ADMIN)
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        }
        String userEmail = "user@user.com";
        if (!memberRepository.existsByEmail(userEmail)) {
            memberRepository.save(
                    Member.builder()
                            .email(userEmail)
                            .name("user")
                            .password(passwordEncoder.encode("user123"))
                            .role(Role.USER)
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        }
    }
}
