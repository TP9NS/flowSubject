package com.flow.fileExtension.web.controller;

import com.flow.fileExtension.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final FileService fileService;

    @GetMapping("/user/files")
    public String userFiles(Authentication auth, Model model) {
        model.addAttribute("files", fileService.myFiles(auth.getName()));
        return "user/files";
    }
}
