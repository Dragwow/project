package com.yumimi.authservice.controller;

import com.yumimi.authservice.dto.request.AuthRequest;
import com.yumimi.authservice.dto.request.LoginVerifyRequest;
import com.yumimi.authservice.dto.response.LoginResponse;
import com.yumimi.authservice.dto.response.MessageResponse;
import com.yumimi.authservice.facade.LoginUserFacade;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginUserFacade loginUserFacade;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletResponse response){
        return ResponseEntity.ok(loginUserFacade.login(request, response));
    }

    @PostMapping("/login/verify")
    public ResponseEntity<MessageResponse> verify(
            @Valid @RequestBody LoginVerifyRequest request,
            HttpServletResponse response){
        return ResponseEntity.ok(loginUserFacade.verifyLogin(request, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletResponse response){
        return ResponseEntity.ok(loginUserFacade.logout(response));
    }
}
