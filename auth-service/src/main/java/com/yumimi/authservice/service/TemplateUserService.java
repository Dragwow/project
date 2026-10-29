package com.yumimi.authservice.service;

import com.yumimi.authservice.dto.request.AuthRequest;
import com.yumimi.authservice.dto.request.RegistrationRequest;
import com.yumimi.authservice.dto.response.AuthResponse;

public interface TemplateUserService {

    RegistrationRequest saveTempUser(RegistrationRequest request);
    AuthRequest saveTempUserByLogin(String email, AuthRequest request);
    AuthResponse saveTempEmail(String email);
    RegistrationRequest getTempUserByEmail(String email);
    AuthRequest getTempUserByLogin(String email);
    void deleteTempUser(String email);

}
