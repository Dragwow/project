package com.yumimi.authservice.entity;

import com.yumimi.utilsservice.basepackages.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pending_login")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PendingLogin extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "hash_code", nullable = false)
    private String hashCode;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "integer default 0")
    private int attempts = 0;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
