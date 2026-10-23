package com.yumimi.authservice.service;

import com.yumimi.authservice.entity.AuthUser;
import com.yumimi.utilsservice.basepackages.CrudService;

import java.util.Optional;

public interface AuthUserService extends CrudService<AuthUser> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<AuthUser> findByUsername(String username);
}
