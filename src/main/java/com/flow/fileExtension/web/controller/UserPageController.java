package com.flow.fileExtension.web.controller;

import com.flow.fileExtension.file.service.FileQueryService;
import com.flow.fileExtension.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final FileQueryService fileQueryService;

    @GetMapping("/user/files")
    public String userFiles(Authentication auth,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {

        var pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        var result = fileQueryService.myFiles(auth.getName(), pageable);

        model.addAttribute("page", result);
        return "user/files";
    }
}

