package com.flow.fileExtension.audit.entity;

import com.flow.fileExtension.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
    name = "extensionChangeLog",
    indexes = {
        @Index(name = "idxExtensionChangeLogCreatedAt", columnList = "createdAt"),
        @Index(name = "idxExtensionChangeLogActorId", columnList = "actorId")
    }
)
public class ExtensionChangeLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actorId")
    private Member actor; // 누가 변경했는지(관리자)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ChangeAction action;

    @Column(nullable = false, length = 20)
    private String targetType; // "FIXED" / "CUSTOM" / "FILE" 등

    @Column(nullable = false, length = 20)
    private String extension; // 대상 확장자

    @Column(length = 50)
    private String beforeValue;

    @Column(length = 50)
    private String afterValue;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
