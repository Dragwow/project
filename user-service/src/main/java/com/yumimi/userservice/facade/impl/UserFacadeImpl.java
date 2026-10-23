package com.yumimi.userservice.facade.impl;

import com.yumimi.userservice.dto.request.UserCreateRequest;
import com.yumimi.userservice.entity.RoleUser;
import com.yumimi.userservice.entity.User;
import com.yumimi.userservice.facade.UserFacade;
import com.yumimi.userservice.service.UserService;
import com.yumimi.userservice.utils.UserCreateResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService service;

    @Override
    public UserCreateResult create(UserCreateRequest request) {
        if (service.existsByAuthUserId(request.getUserId())){
            log.info(
                    "User already exists, skipping event authUserId:{}",
                    request.getUserId());
            return UserCreateResult.SKIPPED_RESULT;
        }
        if (service.existsByUsername(request.getUsername())){
            log.info(
                    "User already exists, skipping event username;{}",
                    request.getUsername());
            return UserCreateResult.SKIPPED_RESULT;
        }
        if (service.existsByEmail(request.getEmail())){
            log.info(
                    "User already exists, skipping event email:{}",
                    request.getEmail());
            return UserCreateResult.SKIPPED_RESULT;
        }
        User user = new User();
        user.setAuthUserId(request.getUserId());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(RoleUser.USER);
        service.create(user);

        return UserCreateResult.CREATED;
    }

    @Override
    public void delete(Long id) {
        service.delete(id);
    }
}
