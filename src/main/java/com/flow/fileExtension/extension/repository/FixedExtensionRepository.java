package com.flow.fileExtension.extension.repository;

import com.flow.fileExtension.extension.entity.FixedExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FixedExtensionRepository extends JpaRepository<FixedExtension, Long> {
    Optional<FixedExtension> findByExtension(String extension);
    List<FixedExtension> findAllByOrderByExtensionAsc();
}
