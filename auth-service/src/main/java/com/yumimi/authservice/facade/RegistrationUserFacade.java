package com.yumimi.authservice.facade;

import com.yumimi.authservice.dto.request.AuthRequest;
import com.yumimi.authservice.dto.request.RegistrationRequest;
import com.yumimi.authservice.dto.response.AuthResponse;

public interface RegistrationUserFacade {

    AuthResponse registerUser(RegistrationRequest request);
}
