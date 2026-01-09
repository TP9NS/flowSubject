package com.flow.fileExtension.audit.controller;

import com.flow.fileExtension.audit.controller.dto.LogResponse;
import com.flow.fileExtension.audit.service.LogService;
import com.flow.fileExtension.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/extensions")
public class LogController {

    private final LogService logService;

    @GetMapping("/logs")
    public ResponseDto<List<LogResponse>> logs(@RequestParam(defaultValue = "20") int limit) {
        return ResponseDto.ok(logService.latest(limit));
    }
}
