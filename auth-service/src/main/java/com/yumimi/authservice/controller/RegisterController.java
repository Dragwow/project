package com.yumimi.authservice.controller;

import com.yumimi.authservice.dto.request.RegistrationRequest;
import com.yumimi.authservice.dto.request.RegisterVerifyRequest;
import com.yumimi.authservice.dto.response.MessageResponse;
import com.yumimi.authservice.facade.RegistrationUserFacade;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegisterController {

    private final RegistrationUserFacade regUser;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody RegistrationRequest request){
        return ResponseEntity.accepted().body(regUser.registerUser(request));
    }

    @PostMapping("/register/verify")
    public ResponseEntity<MessageResponse> verify
            (@Valid @RequestBody RegisterVerifyRequest request,
             HttpServletResponse response){

        return ResponseEntity.ok(regUser.confirmRegistration(request, response));
    }


}
