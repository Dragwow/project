package com.yumimi.authservice.facade.impl;

import com.yumimi.authservice.config.AuthentificationProducer;
import com.yumimi.authservice.dto.request.RegistrationRequest;
import com.yumimi.authservice.dto.response.AuthResponse;
import com.yumimi.authservice.dto.response.MessageResponse;
import com.yumimi.authservice.entity.PendingRegistration;
import com.yumimi.authservice.facade.RegistrationUserFacade;
import com.yumimi.authservice.service.AuthUserService;
import com.yumimi.authservice.service.MailService;
import com.yumimi.authservice.service.OtpService;
import com.yumimi.authservice.service.PendingRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationUserFacadeImpl implements RegistrationUserFacade {

    private static final long OTP_TTL_MINUTES = 5;

    private final AuthUserService authUserService;
    private final PendingRegistrationService pendingRegistrationService;
    private final MailService mailService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MessageResponse registerUser(RegistrationRequest request) {
        if (!request.getPassword().equals(request.getSecondPassword())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Passwords do not match"
            );
        }

        String email = request.getEmail().trim().toLowerCase();
        String username = request.getUsername().trim();

        if (authUserService.existsByUsername(username)){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This username already exists"
            );
        }
        if (authUserService.existsByEmail(email)){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This email already exists"
            );
        }

        LocalDateTime now = LocalDateTime.now();
        pendingRegistrationService.deleteByEmail(email);
        pendingRegistrationService.deleteExpired(now);

        if (pendingRegistrationService.existsByUsername(username)){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This username already taken"
            );
        }

        String code = otpService.generateCode();

        pendingRegistrationService.save(PendingRegistration.builder()
                .email(email)
                .username(username)
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .otpHash(otpService.hash(code))
                .expiresAt(now.plusMinutes(OTP_TTL_MINUTES))
                .createdAt(now)
                .build());

        mailService.sendMessage(
                email,
                "Confirmation Code",
                "Your Confirmation code: " + code + "\nIt is valid for " + OTP_TTL_MINUTES + " minutes"
        );

        return new MessageResponse("Confirmation code sent to " + email);
    }
}
