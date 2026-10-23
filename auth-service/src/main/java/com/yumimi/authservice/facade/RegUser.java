package com.yumimi.authservice.facade;

import com.yumimi.authservice.dto.request.RegistrationRequest;

public interface RegUser {

    void registerUser(RegistrationRequest request);
}
