package com.yumimi.authservice.repository;

import com.yumimi.authservice.entity.AuthUser;
import com.yumimi.utilsservice.basepackages.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends BaseRepository<AuthUser> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<AuthUser> findByUsername(String username);
}
