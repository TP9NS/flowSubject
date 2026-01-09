package com.flow.fileExtension.audit.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LogResponse {
    private Long id;
    private LocalDateTime createdAt;
    private String action;
    private String targetType;
    private String extension;
    private String actorEmail;
}
