package com.flow.fileExtension.extension.service;

import com.flow.fileExtension.audit.entity.ChangeAction;
import com.flow.fileExtension.audit.entity.ExtensionChangeLog;
import com.flow.fileExtension.audit.repository.ExtensionChangeLogRepository;
import com.flow.fileExtension.extension.entity.CustomExtension;
import com.flow.fileExtension.extension.entity.FixedExtension;
import com.flow.fileExtension.extension.repository.CustomExtensionRepository;
import com.flow.fileExtension.extension.repository.FixedExtensionRepository;
import com.flow.fileExtension.global.exception.BusinessException;
import com.flow.fileExtension.global.exception.ErrorCode;
import com.flow.fileExtension.member.entity.Member;
import com.flow.fileExtension.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class ExtensionService {

    private static final int CUSTOM_LIMIT = 200;

    private final FixedExtensionRepository fixedRepo;
    private final CustomExtensionRepository customRepo;
    private final ExtensionChangeLogRepository logRepo;
    private final MemberRepository memberRepository;

    public void toggleFixed(Long fixedId, String actorEmail, boolean blocked) {
        FixedExtension fixed = fixedRepo.findById(fixedId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FIXED_NOT_FOUND, "파일 저장 실패"));

        boolean before = fixed.isBlocked();
        if (before == blocked) return;

        fixed.setBlocked(blocked);
        fixedRepo.save(fixed);

        Member actor = memberRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "파일 저장 실패"));

        logRepo.save(ExtensionChangeLog.builder()
                .actor(actor)
                .action(blocked ? ChangeAction.CHECK : ChangeAction.UNCHECK)
                .targetType("FIXED")
                .extension(fixed.getExtension())
                .beforeValue(String.valueOf(before))
                .afterValue(String.valueOf(blocked))
                .build());
    }

    public void addCustom(String rawExtension, String actorEmail) {
        String ext = normalize(rawExtension);

        validateCustom(ext);

        if (customRepo.count() >= CUSTOM_LIMIT) {
            throw new BusinessException(ErrorCode.CUSTOM_LIMIT_EXCEEDED, "파일 저장 실패");
        }

        if (customRepo.existsByExtension(ext)) {
            throw new BusinessException(ErrorCode.CUSTOM_DUPLICATED, "파일 저장 실패");
        }

        CustomExtension saved = customRepo.save(CustomExtension.builder()
                .extension(ext)
                .build());

        Member actor = memberRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "파일 저장 실패"));

        logRepo.save(ExtensionChangeLog.builder()
                .actor(actor)
                .action(ChangeAction.CREATE)
                .targetType("CUSTOM")
                .extension(saved.getExtension())
                .beforeValue(null)
                .afterValue("created")
                .build());
    }

    public void deleteCustom(Long customId, String actorEmail) {
        CustomExtension custom = customRepo.findById(customId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOM_NOT_FOUND, "파일 저장 실패"));

        customRepo.delete(custom);

        Member actor = memberRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "파일 저장 실패"));

        logRepo.save(ExtensionChangeLog.builder()
                .actor(actor)
                .action(ChangeAction.DELETE)
                .targetType("CUSTOM")
                .extension(custom.getExtension())
                .beforeValue("exists")
                .afterValue(null)
                .build());
    }

    @Transactional(readOnly = true)
    public boolean isBlocked(String rawExtension) {
        String ext = normalize(rawExtension);

        if (ext.isBlank()) {
            throw new BusinessException(ErrorCode.EXTENSION_INVALID, "파일 저장 실패");
        }

        // custom에 있으면 차단
        if (customRepo.existsByExtension(ext)) return true;

        // fixed 차단 체크
        return fixedRepo.findByExtension(ext).map(FixedExtension::isBlocked).orElse(false);
    }

    @Transactional(readOnly = true)
    public long customCount() {
        return customRepo.count();
    }

    private String normalize(String raw) {
        if (raw == null) return "";
        String v = raw.trim().toLowerCase(Locale.ROOT);
        if (v.startsWith(".")) v = v.substring(1);
        return v;
    }

    private void validateCustom(String ext) {
        if (ext.isBlank()) throw new BusinessException(ErrorCode.EXTENSION_INVALID, "파일 저장 실패");
        if (ext.length() > 20) throw new BusinessException(ErrorCode.EXTENSION_INVALID, "파일 저장 실패");
        if (!ext.matches("^[a-z0-9]+$")) throw new BusinessException(ErrorCode.EXTENSION_INVALID, "파일 저장 실패");
    }
}
