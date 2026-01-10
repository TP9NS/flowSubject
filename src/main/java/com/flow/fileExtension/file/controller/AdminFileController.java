package com.flow.fileExtension.file.controller;

import com.flow.fileExtension.file.service.FileManageService;
import com.flow.fileExtension.file.service.FileQueryService;
import com.flow.fileExtension.global.dto.PageResponseDto;
import com.flow.fileExtension.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/files")
public class AdminFileController {

    private final FileQueryService fileQueryService;
    private final FileManageService fileManageService;

    @GetMapping
    public ResponseDto<?> list(@RequestParam(required = false) String ext,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "3") int size) {
        int p = Math.max(page, 0);
        int s = Math.min(Math.max(size, 1), 100);

        var result = fileQueryService.adminFiles(ext, PageRequest.of(p, s));
        return ResponseDto.ok(PageResponseDto.from(result));
    }


    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        var dto = fileManageService.downloadForAdmin(id);

        String encoded = URLEncoder.encode(dto.getOriginalFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
                .body(dto.getResource());
    }

    @DeleteMapping("/{id}")
    public ResponseDto<Void> delete(@PathVariable Long id) {
        fileManageService.deleteForAdmin(id);
        return ResponseDto.ok();
    }
}
