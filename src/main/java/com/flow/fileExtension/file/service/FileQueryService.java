package com.flow.fileExtension.file.service;

import com.flow.fileExtension.file.controller.dto.FileItemDto;
import com.flow.fileExtension.file.repository.FileUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileQueryService {

    private final FileUploadRepository fileUploadRepository;

    public Page<FileItemDto> myFiles(String email, Pageable pageable) {
        return fileUploadRepository.findMyFiles(email, pageable);
    }

    public Page<FileItemDto> adminFiles(String ext, Pageable pageable) {
        if (ext == null || ext.isBlank()) {
            return fileUploadRepository.findAllFiles(pageable);
        }
        return fileUploadRepository.findAllFilesByExtension(ext.trim().toLowerCase(), pageable);
    }
}
