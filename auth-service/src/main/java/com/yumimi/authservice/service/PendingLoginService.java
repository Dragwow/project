package com.yumimi.authservice.service;

import com.yumimi.authservice.entity.PendingLogin;
import com.yumimi.authservice.entity.PendingRegistration;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PendingLoginService {

    Optional<PendingLogin> findByUsername(String username);

    void deleteByUsername(String username);

    PendingLogin save(PendingLogin pending);

    void deleteExpired(LocalDateTime now);
}
