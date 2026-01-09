package com.flow.fileExtension.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {
    @GetMapping("/user/files")
    public String userFiles() { return "user/files"; }
}
