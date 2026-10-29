package com.yumimi.authservice.repository;

import com.yumimi.authservice.entity.PendingRegistration;
import com.yumimi.utilsservice.basepackages.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingRegistrationRepository extends BaseRepository<PendingRegistration> {

    Optional<PendingRegistration> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void deleteByEmail(String email);
}
