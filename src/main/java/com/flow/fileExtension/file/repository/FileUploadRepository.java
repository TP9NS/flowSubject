package com.flow.fileExtension.file.repository;

import com.flow.fileExtension.file.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

    @Query("""
        select f from FileUpload f
        join fetch f.uploader u
        where u.email = :email
        order by f.uploadedAt desc
    """)
    List<FileUpload> findMyFiles(@Param("email") String email);

    @Query("""
        select f from FileUpload f
        join fetch f.uploader u
        order by f.uploadedAt desc
    """)
    List<FileUpload> findAllWithUploader();
}
