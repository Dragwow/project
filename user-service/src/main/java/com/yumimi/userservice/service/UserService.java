package com.yumimi.userservice.service;

import com.yumimi.userservice.entity.User;
import com.yumimi.utilsservice.basepackages.CrudService;

public interface UserService extends CrudService<User> {



    boolean existsByEmail(String email);
    boolean existsByAuthUserId(Long authUserId);
    boolean existsByUsername(String username);
}
