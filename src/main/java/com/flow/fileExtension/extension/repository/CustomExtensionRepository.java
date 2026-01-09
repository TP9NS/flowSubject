package com.flow.fileExtension.extension.repository;

import com.flow.fileExtension.extension.entity.CustomExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomExtensionRepository extends JpaRepository<CustomExtension, Long> {
    Optional<CustomExtension> findByExtension(String extension);
    boolean existsByExtension(String extension);
    long count();
    List<CustomExtension> findAllByOrderByCreatedAtDesc();
}
