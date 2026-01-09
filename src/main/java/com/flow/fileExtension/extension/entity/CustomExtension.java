package com.flow.fileExtension.extension.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
    name = "customExtension",
    uniqueConstraints = {
        @UniqueConstraint(name = "uqCustomExtensionExtension", columnNames = "extension")
    },
    indexes = {
        @Index(name = "idxCustomExtensionCreatedAt", columnList = "createdAt")
    }
)
public class CustomExtension {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String extension;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
