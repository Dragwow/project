package com.yumimi.authservice.service;

import com.yumimi.authservice.entity.PendingRegistration;

import java.util.Optional;

public interface PendingRegistrationService {

    Optional<PendingRegistration> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void deleteByEmail(String email);
}
