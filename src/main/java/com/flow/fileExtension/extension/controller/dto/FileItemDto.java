package com.flow.fileExtension.extension.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FileItemDto {
    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String storagePath;
    private String extension;
    private long sizeBytes;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String uploaderEmail; // 관리자 조회용
}
