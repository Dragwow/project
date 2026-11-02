package com.yumimi.authservice.service.impl;

import com.yumimi.authservice.entity.PendingRegistration;
import com.yumimi.authservice.repository.PendingRegistrationRepository;
import com.yumimi.authservice.service.PendingRegistrationService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PendingRegistrationServiceImpl implements PendingRegistrationService {

    private final PendingRegistrationRepository repository;

    @Override
    public Optional<PendingRegistration> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public PendingRegistration save(PendingRegistration pending) {
        return repository.save(pending);
    }

    @Override
    public void deleteByEmail(String email) {
        repository.deleteByEmail(email);
    }

    @Override
    public void deleteExpired(LocalDateTime now) {
        repository.deleteExpired(now);
    }
}
