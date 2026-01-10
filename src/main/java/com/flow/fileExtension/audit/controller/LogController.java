package com.flow.fileExtension.audit.controller;

import com.flow.fileExtension.audit.controller.dto.LogResponse;
import com.flow.fileExtension.audit.service.LogService;
import com.flow.fileExtension.global.dto.PageResponseDto;
import com.flow.fileExtension.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/extensions")
public class LogController {

    private final LogService logService;

    @GetMapping("/logs")
    public ResponseDto<PageResponseDto<LogResponse>> logs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseDto.ok(logService.page(page, size));
    }
}
