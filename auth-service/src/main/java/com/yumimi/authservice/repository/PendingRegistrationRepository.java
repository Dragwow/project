package com.yumimi.authservice.repository;

import com.yumimi.authservice.entity.PendingRegistration;
import com.yumimi.utilsservice.basepackages.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PendingRegistrationRepository extends BaseRepository<PendingRegistration> {

    Optional<PendingRegistration> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from PendingRegistration p where p.email = :email")
    void deleteByEmail(@Param("email") String email);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from PendingRegistration p where p.expiresAt = :now")
    void deleteExpired(@Param("now") LocalDateTime now);
}
