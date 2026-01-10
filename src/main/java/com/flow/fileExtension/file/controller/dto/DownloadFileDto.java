package com.flow.fileExtension.file.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.Resource;

@Getter
@AllArgsConstructor
public class DownloadFileDto {
    private String originalFileName;
    private Resource resource;
}
