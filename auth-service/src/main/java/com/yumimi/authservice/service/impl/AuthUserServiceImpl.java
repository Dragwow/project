package com.yumimi.authservice.service.impl;

import com.yumimi.authservice.entity.AuthUser;
import com.yumimi.authservice.repository.AuthUserRepository;
import com.yumimi.authservice.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository repository;

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Optional<AuthUser> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public void create(AuthUser entity) {
        repository.save(entity);
    }

    @Override
    public void update(AuthUser entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public AuthUser findById(Long id) {
        return null;
    }
}
