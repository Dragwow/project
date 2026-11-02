package com.yumimi.authservice.facade;

import com.yumimi.authservice.dto.request.AuthRequest;
import com.yumimi.authservice.dto.request.RegistrationRequest;
import com.yumimi.authservice.dto.response.AuthResponse;
import com.yumimi.authservice.dto.response.MessageResponse;

public interface RegistrationUserFacade {

    MessageResponse registerUser(RegistrationRequest request);
}
