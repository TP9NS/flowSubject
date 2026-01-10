package com.flow.fileExtension.web.controller;

import com.flow.fileExtension.audit.repository.ExtensionChangeLogRepository;
import com.flow.fileExtension.audit.service.LogService;
import com.flow.fileExtension.extension.repository.CustomExtensionRepository;
import com.flow.fileExtension.extension.repository.FixedExtensionRepository;
import com.flow.fileExtension.file.service.FileQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminPageController {

    private final FixedExtensionRepository fixedRepo;
    private final CustomExtensionRepository customRepo;
    private final LogService logService;
    private final FileQueryService fileQueryService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("fixedList", fixedRepo.findAllByOrderByExtensionAsc());
        model.addAttribute("customList", customRepo.findAllByOrderByCreatedAtDesc());
        model.addAttribute("customCount", customRepo.count());
        return "admin/admin";
    }

    @GetMapping("/admin/files")
    public String adminFilesPage() {
        return "admin/files";
    }
}
