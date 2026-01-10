package com.flow.fileExtension.file.controller;

import com.flow.fileExtension.file.service.FileService;
import com.flow.fileExtension.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/files")
public class FileController {

    private final FileService fileService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseDto<Void> upload(@RequestPart("file") MultipartFile file,
                                    Authentication auth) {
        fileService.upload(file, auth.getName());
        return ResponseDto.ok();
    }
}
