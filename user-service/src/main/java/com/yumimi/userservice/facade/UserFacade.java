package com.yumimi.userservice.facade;

import com.yumimi.userservice.dto.request.UserCreateRequest;
import com.yumimi.userservice.utils.UserCreateResult;

public interface UserFacade {

    UserCreateResult create(UserCreateRequest request);
//    void update()
    void delete(Long id);
}
