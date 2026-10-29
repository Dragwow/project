package com.yumimi.authservice.facade.impl;

import com.yumimi.authservice.config.AuthentificationProducer;
import com.yumimi.authservice.dto.request.AuthRequest;
import com.yumimi.authservice.dto.request.RegistrationRequest;
import com.yumimi.authservice.dto.response.AuthResponse;
import com.yumimi.authservice.facade.RegistrationUserFacade;
import com.yumimi.authservice.service.AuthUserService;
import com.yumimi.authservice.service.OtpService;
import com.yumimi.authservice.service.TemplateUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationUserFacadeImpl implements RegistrationUserFacade {

    private final AuthUserService authUserService;
    private final PasswordEncoder passwordEncoder;
    private final AuthentificationProducer producer;
    private final TemplateUserService templateUserService;
    private final OtpService otpService;

    @Override
    public AuthResponse registerUser(RegistrationRequest request) {
    if (request.getPassword() != null || request.getSecondPassword() != null
            && !request.getPassword().equals(request.getSecondPassword())){
        throw new IllegalArgumentException("Password are incorrect");}

    templateUserService.saveTempUser(request);
    otpService.sendOtp(request.getEmail());
    return new AuthResponse("Otp code send: " + request.getEmail());
    }

//     if (!request.getPassword().equals(request.getSecondPassword())){
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Passwords do not match"
//            );
//        }
//        if (service.existsByUsername(request.getUsername())){
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "This username already exists"
//            );
//        }
//        if (service.existsByEmail(request.getEmail())){
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "This email already exists"
//            );
//        }
//        AuthUser authUser = new AuthUser();
//        authUser.setUsername(request.getUsername());
//        authUser.setEmail(request.getEmail());
//        authUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
//
//        service.create(authUser);
//
//        UserCreateEvent event = new UserCreateEvent(
//                authUser.getId(),
//                request.getUsername(),
//                request.getEmail(),
//                request.getFirstName(),
//                request.getLastName()
//
//        );
//
//        producer.sendUserCreatingTemplate(event);
}
