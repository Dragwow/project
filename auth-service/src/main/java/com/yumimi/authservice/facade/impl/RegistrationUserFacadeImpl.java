package com.yumimi.authservice.facade.impl;

import com.yumimi.authservice.config.AuthCookieService;
import com.yumimi.authservice.dto.request.RegistrationRequest;
import com.yumimi.authservice.dto.request.RegisterVerifyRequest;
import com.yumimi.authservice.dto.response.MessageResponse;
import com.yumimi.authservice.entity.AuthUser;
import com.yumimi.authservice.entity.PendingRegistration;
import com.yumimi.authservice.facade.RegistrationUserFacade;
import com.yumimi.authservice.service.*;
import com.yumimi.eventsservice.events.UserCreateEvent;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static com.yumimi.authservice.utils.StaticStringUtils.*;

@Service
@RequiredArgsConstructor
public class RegistrationUserFacadeImpl implements RegistrationUserFacade {



    private final AuthUserService authUserService;
    private final PendingRegistrationService pendingRegistrationService;
    private final MailService mailService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthCookieService cookieService;
    private final ApplicationEventPublisher applicationEventPublisher;

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
                .expiresAt(now.plusMinutes(CODE_TTL_MINUTES))
                .createdAt(now)
                .build());

        mailService.sendMessage(
                email,
                "Confirmation Code",
                "Your Confirmation code: " + code + "\nIt is valid for " + CODE_TTL_MINUTES + " minutes"
        );

        return new MessageResponse("Confirmation code sent to " + email);
    }

    @Override
    @Transactional(noRollbackFor = ResponseStatusException.class)
    public MessageResponse confirmRegistration(RegisterVerifyRequest request, HttpServletResponse response) {
        String email = request.getEmail().trim().toLowerCase();

        PendingRegistration pending = pendingRegistrationService.findByEmail(email)
                .orElseThrow(this::invalidCode);

        if (pending.getExpiresAt().isBefore(LocalDateTime.now())
                || pending.getAttempts() >= MAX_ATTEMPTS){
            pendingRegistrationService.deleteByEmail(email);
            throw invalidCode();
        }

        if (!otpService.matches(request.getCode(), pending.getOtpHash())){
            pending.setAttempts(pending.getAttempts() + 1);
            pendingRegistrationService.save(pending);
            throw invalidCode();
        }

        if (authUserService.existsByEmail(pending.getEmail())
                || authUserService.existsByUsername(pending.getUsername())){
            pendingRegistrationService.deleteByEmail(email);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email or Username already taken");
        }

        AuthUser user = new AuthUser();
        user.setUsername(pending.getUsername());
        user.setEmail(pending.getEmail());
        user.setPasswordHash(pending.getPasswordHash());

        authUserService.create(user);

        applicationEventPublisher.publishEvent(new UserCreateEvent(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                pending.getFirstName(),
                pending.getLastName()
        ));

        pendingRegistrationService.deleteByEmail(email);

        String token = jwtService.generateToken(user.getUsername());
        cookieService.addToken(response, token);

        return new MessageResponse("Registration Completed");

    }

    private ResponseStatusException invalidCode(){
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired code");
    }
}
