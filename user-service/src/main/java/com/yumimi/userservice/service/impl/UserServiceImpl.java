package com.yumimi.userservice.service.impl;

import com.yumimi.userservice.entity.User;
import com.yumimi.userservice.repository.UserRepository;
import com.yumimi.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public void create(User entity) {
        repository.save(entity);
    }

    @Override
    public void update(User entity) {
        repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByAuthUserId(Long authUserId) {
        return repository.existsByAuthUserId(authUserId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }
}
