package com.flow.fileExtension.file.entity;

import com.flow.fileExtension.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
        name = "fileUpload",
        indexes = {
                @Index(name = "idxFileUploadUploaderId", columnList = "uploaderId"),
                @Index(name = "idxFileUploadUploadedAt", columnList = "uploadedAt")
        }
)
public class FileUpload {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploaderId", nullable = false)
    private Member uploader;

    @Column(nullable = false, length = 255)
    private String originalFileName;

    @Column(nullable = false, length = 255)
    private String storedFileName; // UUID + 확장자 등

    @Column(nullable = false, length = 500)
    private String storagePath; // 로컬 저장 경로

    @Column(nullable = false, length = 20)
    private String extension;

    @Column(nullable = false)
    private long sizeBytes;

    @Column(length = 100)
    private String contentType;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }
}
