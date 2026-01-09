package com.flow.fileExtension.extension.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
    name = "fixedExtension",
    uniqueConstraints = {
        @UniqueConstraint(name = "uqFixedExtensionExtension", columnNames = "extension")
    }
)
public class FixedExtension {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String extension; // exe, sh...

    @Column(nullable = false)
    private boolean blocked; // 체크 여부

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist @PreUpdate
    void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
