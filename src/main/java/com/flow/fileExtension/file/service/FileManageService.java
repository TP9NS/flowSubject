package com.flow.fileExtension.file.service;

import com.flow.fileExtension.file.controller.dto.DownloadFileDto;
import com.flow.fileExtension.file.entity.FileUpload;
import com.flow.fileExtension.file.repository.FileUploadRepository;

import com.flow.fileExtension.global.exception.BusinessException;
import com.flow.fileExtension.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Transactional
public class FileManageService {

    private final FileUploadRepository fileUploadRepository;

    @Value("${app.file.uploadDir}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public DownloadFileDto downloadForUser(Long fileId, String email) {
        if (!fileUploadRepository.existsMyFile(fileId, email)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인 파일만 다운로드할 수 있습니다.");
        }
        FileUpload file = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "파일을 찾을 수 없습니다."));
        Resource resource = toSafeResource(file);
        return new DownloadFileDto(file.getOriginalFileName(), resource);
    }

    @Transactional(readOnly = true)
    public DownloadFileDto downloadForAdmin(Long fileId) {
        FileUpload file = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "파일을 찾을 수 없습니다."));
        Resource resource = toSafeResource(file);
        return new DownloadFileDto(file.getOriginalFileName(), resource);
    }

    public void deleteForUser(Long fileId, String email) {
        if (!fileUploadRepository.existsMyFile(fileId, email)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인 파일만 삭제할 수 있습니다.");
        }
        deleteInternal(fileId);
    }

    public void deleteForAdmin(Long fileId) {
        deleteInternal(fileId);
    }

    private void deleteInternal(Long fileId) {
        FileUpload file = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "파일을 찾을 수 없습니다."));

        try {
            Path p = safePath(file.getStoragePath());
            Files.deleteIfExists(p);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "파일 삭제 중 오류가 발생했습니다.");
        }

        fileUploadRepository.delete(file);
    }

    private Resource toSafeResource(FileUpload file) {
        try {
            Path p = safePath(file.getStoragePath());
            if (!Files.exists(p)) {
                throw new BusinessException(ErrorCode.NOT_FOUND, "저장된 파일이 존재하지 않습니다.");
            }
            return new UrlResource(p.toUri());
        } catch (MalformedURLException e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "파일 경로가 올바르지 않습니다.");
        }
    }

    private Path safePath(String storagePath) {
        Path root = Path.of(uploadDir).toAbsolutePath().normalize();
        Path target = Path.of(storagePath).toAbsolutePath().normalize();
        if (!target.startsWith(root)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "허용되지 않은 경로입니다.");
        }
        return target;
    }
}
