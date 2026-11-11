package com.yumimi.authservice.repository;

import com.yumimi.authservice.entity.PendingLogin;
import com.yumimi.utilsservice.basepackages.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PendingLoginRepository extends BaseRepository<PendingLogin> {

    Optional<PendingLogin> findByUsername(String username);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from PendingLogin c where c.username = :username")
    void deleteByUsername(@Param("username") String username);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from PendingLogin c where c.expiresAt <= :now")
    void deleteExpired(@Param("now") LocalDateTime now);

}
