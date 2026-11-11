package com.yumimi.authservice.service.impl;

import com.yumimi.authservice.entity.PendingLogin;
import com.yumimi.authservice.repository.PendingLoginRepository;
import com.yumimi.authservice.service.PendingLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.yumimi.authservice.utils.StaticStringUtils.RESEND_COOLDOWN_SECONDS;

@Service
@RequiredArgsConstructor
public class PendingLoginServiceImpl implements PendingLoginService {

    private final PendingLoginRepository repository;

    @Override
    public Optional<PendingLogin> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public void deleteByUsername(String username) {
        repository.deleteByUsername(username);
    }

    @Override
    public PendingLogin save(PendingLogin pending) {
        return repository.save(pending);
    }

    @Override
    public void deleteExpired(LocalDateTime now) {
        repository.deleteExpired(now);
    }
}
