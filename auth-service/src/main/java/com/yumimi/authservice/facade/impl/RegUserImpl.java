package com.yumimi.authservice.facade.impl;

import com.yumimi.authservice.config.AuthentificationProducer;
import com.yumimi.authservice.dto.request.RegistrationRequest;
import com.yumimi.authservice.entity.AuthUser;
import com.yumimi.authservice.facade.RegUser;
import com.yumimi.authservice.service.AuthUserService;
import com.yumimi.eventsservice.events.UserCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegUserImpl implements RegUser {

    private final AuthUserService service;
    private final PasswordEncoder passwordEncoder;
    private final AuthentificationProducer producer;

    @Override
    public void registerUser(RegistrationRequest request) {
        if (!request.getPassword().equals(request.getSecondPassword())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Passwords do not match"
            );
        }
        if (service.existsByUsername(request.getUsername())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This username already exists"
            );
        }
        if (service.existsByEmail(request.getEmail())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This email already exists"
            );
        }
        AuthUser authUser = new AuthUser();
        authUser.setUsername(request.getUsername());
        authUser.setEmail(request.getEmail());
        authUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        service.create(authUser);

        UserCreateEvent event = new UserCreateEvent(
                authUser.getId(),
                request.getUsername(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName()

        );

        producer.sendUserCreatingTemplate(event);
    }
}
