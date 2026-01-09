package com.flow.fileExtension.audit.service;

import com.flow.fileExtension.audit.repository.ExtensionChangeLogRepository;
import com.flow.fileExtension.audit.controller.dto.LogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogService {

    private final ExtensionChangeLogRepository logRepository;

    public List<LogResponse> latest(int size) {
        int safe = Math.min(Math.max(size, 1), 200);

        var page = logRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, safe));
        return page.getContent().stream()
                .map(log -> new LogResponse(
                        log.getId(),
                        log.getCreatedAt(),
                        log.getAction().name(),
                        log.getTargetType(),
                        log.getExtension(),
                        (log.getActor() != null ? log.getActor().getEmail() : "-")
                ))
                .toList();
    }
}
