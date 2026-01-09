package com.flow.fileExtension.audit.repository;

import com.flow.fileExtension.audit.entity.ExtensionChangeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface ExtensionChangeLogRepository extends JpaRepository<ExtensionChangeLog, Long> {
    Page<ExtensionChangeLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
