package com.flow.fileExtension.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
    @GetMapping("/admin")
    public String adminPage() { return "admin/admin"; }

    @GetMapping("/admin/files")
    public String adminFiles() { return "admin/files"; }
}
