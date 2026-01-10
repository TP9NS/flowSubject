package com.flow.fileExtension.file.repository;

import com.flow.fileExtension.file.controller.dto.FileItemDto;
import com.flow.fileExtension.file.entity.FileUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

    @Query("""
        select new com.flow.fileExtension.file.controller.dto.FileItemDto(
            f.id, f.originalFileName, f.storedFileName, f.storagePath,
            f.extension, f.sizeBytes, f.contentType, f.uploadedAt, u.email
        )
        from FileUpload f
        join f.uploader u
        where u.email = :email
        order by f.uploadedAt desc
    """)
    Page<FileItemDto> findMyFiles(@Param("email") String email, Pageable pageable);

    @Query("""
        select new com.flow.fileExtension.file.controller.dto.FileItemDto(
            f.id, f.originalFileName, f.storedFileName, f.storagePath,
            f.extension, f.sizeBytes, f.contentType, f.uploadedAt, u.email
        )
        from FileUpload f
        join f.uploader u
        order by f.uploadedAt desc
    """)
    Page<FileItemDto> findAllFiles(Pageable pageable);

    @Query("""
        select new com.flow.fileExtension.file.controller.dto.FileItemDto(
            f.id, f.originalFileName, f.storedFileName, f.storagePath,
            f.extension, f.sizeBytes, f.contentType, f.uploadedAt, u.email
        )
        from FileUpload f
        join f.uploader u
        where f.extension = :ext
        order by f.uploadedAt desc
    """)
    Page<FileItemDto> findAllFilesByExtension(@Param("ext") String ext, Pageable pageable);

    @Query("""
        select count(f) > 0
        from FileUpload f
        join f.uploader u
        where f.id = :id and u.email = :email
    """)
    boolean existsMyFile(@Param("id") Long id, @Param("email") String email);
}
