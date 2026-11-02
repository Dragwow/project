package com.yumimi.authservice.service;

import com.yumimi.authservice.entity.PendingRegistration;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PendingRegistrationService {

    Optional<PendingRegistration> findByEmail(String email);

    boolean existsByUsername(String username);

    PendingRegistration save(PendingRegistration pending);

    void deleteByEmail(String email);

    void deleteExpired(LocalDateTime now);
}
