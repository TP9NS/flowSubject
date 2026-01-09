package com.flow.fileExtension.extension.controller;

import com.flow.fileExtension.global.dto.ResponseDto;
import com.flow.fileExtension.extension.service.ExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/extensions")
public class ExtensionController {

    private final ExtensionService extensionService;

    // fixed 체크/해제
    @PostMapping("/fixed/{id}")
    public ResponseDto<Void> toggleFixed(@PathVariable Long id,
                                         @RequestParam boolean blocked,
                                         Authentication authentication) {
        extensionService.toggleFixed(id, authentication.getName(), blocked);
        return ResponseDto.ok();
    }

    @PostMapping("/custom")
    public ResponseDto<Void> addCustom(@RequestParam String extension,
                                       Authentication authentication) {
        extensionService.addCustom(extension, authentication.getName());
        return ResponseDto.ok();
    }

    @DeleteMapping("/custom/{id}")
    public ResponseDto<Void> deleteCustom(@PathVariable Long id,
                                          Authentication authentication) {
        extensionService.deleteCustom(id, authentication.getName());
        return ResponseDto.ok();
    }

    @GetMapping("/custom/count")
    public ResponseDto<Long> customCount() {
        return ResponseDto.ok(extensionService.customCount());
    }


    // 요청 DTO
    @lombok.Getter
    public static class AddCustomRequest {
        private String extension;
    }
}
