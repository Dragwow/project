package com.yumimi.authservice.facade;

import com.yumimi.authservice.dto.request.RegistrationRequest;
import com.yumimi.authservice.dto.request.RegisterVerifyRequest;
import com.yumimi.authservice.dto.response.MessageResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface RegistrationUserFacade {

    MessageResponse registerUser(RegistrationRequest request);
    MessageResponse confirmRegistration(RegisterVerifyRequest request, HttpServletResponse response);
}
