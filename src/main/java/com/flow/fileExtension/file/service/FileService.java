package com.flow.fileExtension.file.service;

import com.flow.fileExtension.extension.service.ExtensionService;
import com.flow.fileExtension.file.entity.FileUpload;
import com.flow.fileExtension.file.repository.FileUploadRepository;
import com.flow.fileExtension.global.exception.BusinessException;
import com.flow.fileExtension.global.exception.ErrorCode;
import com.flow.fileExtension.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final FileUploadRepository fileUploadRepository;
    private final MemberRepository memberRepository;
    private final ExtensionService extensionService;

    @Value("${app.file.uploadDir:uploads}")
    private String uploadDir;

    public void upload(MultipartFile file, String uploaderEmail) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "파일 저장 실패");
        }

        String original = file.getOriginalFilename();
        if (original == null || original.isBlank()) original = "file";

        String ext = extractExtension(original);

        if (extensionService.isBlocked(ext)) {
            throw new BusinessException(ErrorCode.BLOCKED_EXTENSION, "파일 저장 실패");
        }

        var uploader = memberRepository.findByEmail(uploaderEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "파일 저장 실패"));

        try {
            Path uploadPath = Path.of(uploadDir).toAbsolutePath();
            Files.createDirectories(uploadPath);

            String stored = UUID.randomUUID() + "_" + sanitize(original);
            Path target = Path.of(uploadDir, stored);

            file.transferTo(target.toFile());

            FileUpload entity = FileUpload.builder()
                    .uploader(uploader)
                    .originalFileName(original)
                    .storedFileName(stored)
                    .storagePath(target.toString())
                    .extension(ext)
                    .sizeBytes(file.getSize())
                    .contentType(file.getContentType())
                    .build();

            fileUploadRepository.saveAndFlush(entity);

        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "파일 저장 실패");
        }
    }

    // Windows 파일명 금지문자 방어
    private String sanitize(String name) {
        return name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    @Transactional(readOnly = true)
    public List<FileUpload> myFiles(String email) {
        return fileUploadRepository.findMyFiles(email);
    }

    @Transactional(readOnly = true)
    public List<FileUpload> allFiles() {
        return fileUploadRepository.findAllWithUploader();
    }

    private String extractExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) return "";
        String v = filename.substring(dot + 1).trim().toLowerCase(Locale.ROOT);
        if (v.startsWith(".")) v = v.substring(1);
        return v;
    }
}
