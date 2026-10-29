package com.yumimi.authservice.entity;

import com.yumimi.utilsservice.basepackages.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpEntry extends BaseEntity {

    private String email;

    private String code;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean used;

}
