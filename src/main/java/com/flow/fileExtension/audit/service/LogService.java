package com.flow.fileExtension.audit.service;

import com.flow.fileExtension.audit.controller.dto.LogResponse;
import com.flow.fileExtension.audit.repository.ExtensionChangeLogRepository;
import com.flow.fileExtension.global.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogService {

    private final ExtensionChangeLogRepository logRepository;

    public PageResponseDto<LogResponse> page(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 200);

        var pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        var p = logRepository.findAll(pageable);

        var mapped = p.map(log -> new LogResponse(
                log.getId(),
                log.getCreatedAt(),
                log.getAction().name(),
                log.getTargetType(),
                log.getExtension(),
                (log.getActor() != null ? log.getActor().getEmail() : "-")
        ));

        return PageResponseDto.from(mapped);
    }
}
