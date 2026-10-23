package com.yumimi.userservice.repository;

import com.yumimi.userservice.entity.User;
import com.yumimi.utilsservice.basepackages.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User> {

    boolean existsByEmail(String email);
    boolean existsByAuthUserId(Long authUserId);
    boolean existsByUsername(String username);
}
