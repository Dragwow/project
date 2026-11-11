package com.yumimi.authservice.facade;

import com.yumimi.authservice.dto.request.AuthRequest;
import com.yumimi.authservice.dto.request.LoginVerifyRequest;
import com.yumimi.authservice.dto.response.LoginResponse;
import com.yumimi.authservice.dto.response.MessageResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginUserFacade {

    LoginResponse login(AuthRequest request, HttpServletResponse response);

    MessageResponse verifyLogin(LoginVerifyRequest request, HttpServletResponse response);

    MessageResponse logout(HttpServletResponse response);
}
