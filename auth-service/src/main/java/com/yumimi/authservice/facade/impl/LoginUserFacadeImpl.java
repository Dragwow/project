package com.yumimi.authservice.facade.impl;

import com.yumimi.authservice.config.AuthCookieService;
import com.yumimi.authservice.dto.request.AuthRequest;
import com.yumimi.authservice.dto.request.LoginVerifyRequest;
import com.yumimi.authservice.dto.response.LoginResponse;
import com.yumimi.authservice.dto.response.MessageResponse;
import com.yumimi.authservice.entity.AuthUser;
import com.yumimi.authservice.entity.PendingLogin;
import com.yumimi.authservice.facade.LoginUserFacade;
import com.yumimi.authservice.service.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static com.yumimi.authservice.utils.StaticStringUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserFacadeImpl implements LoginUserFacade {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthCookieService authCookieService;
    private final OtpService otpService;
    private final PendingLoginService pendingLoginService;
    private final MailService mailService;
    private final AuthUserService authUserService;

    @Value("${app.login.verification-enabled:true}")
    private boolean verificationEnabled;

    @Override
    @Transactional(noRollbackFor = ResponseStatusException.class)
    public LoginResponse login(AuthRequest request, HttpServletResponse response) {
        AuthUser user = authenticate(request.getUsername().trim(), request.getPassword());

        if (!verificationEnabled){
            authCookieService.addToken(response, jwtService.generateToken(user.getUsername()));
            log.info("Login succeeded, username={}", user.getUsername());
            return new LoginResponse("Login completed", false);
        }

        LocalDateTime now = LocalDateTime.now();
        pendingLoginService.deleteExpired(now);

        pendingLoginService.findByUsername(user.getUsername())
                .ifPresent(existing -> {
                    if (existing.getCreatedAt()
                            .plusSeconds(RESEND_COOLDOWN_SECONDS)
                            .isAfter(now)){
                    throw new ResponseStatusException(
                            HttpStatus.TOO_MANY_REQUESTS,
                            "Code was sent recently, try again in a minute"
                    );}
                });
        pendingLoginService.deleteByUsername(user.getUsername());

        String code = otpService.generateCode();
        pendingLoginService.save(PendingLogin.builder()
                .username(user.getUsername())
                .hashCode(otpService.hash(code))
                .expiresAt(now.plusMinutes(CODE_TTL_MINUTES))
                .createdAt(now)
                .build());

        mailService.sendMessage(
                user.getEmail(),
                "Login code",
                "Your login code: " + code + "\nIt is valid for " + CODE_TTL_MINUTES
                        + " minutes . If this not you, change your password.");

        log.info("Login code sent: username={}", user.getUsername());
        return new LoginResponse("Verification code sent to your email", true);
    }

    @Override
    @Transactional(noRollbackFor = ResponseStatusException.class)
    public MessageResponse verifyLogin(LoginVerifyRequest request, HttpServletResponse response) {
        String username = request.getUsername().trim();

        PendingLogin pending = pendingLoginService.findByUsername(username)
                .orElseThrow(() -> invalidCode(username));

        if (pending.getExpiresAt().isBefore(LocalDateTime.now())
                || pending.getAttempts() >= MAX_ATTEMPTS){
            pendingLoginService.deleteByUsername(username);
            throw invalidCode(username);
        }

        if (!otpService.matches(request.getCode(), pending.getHashCode())){
            pending.setAttempts(pending.getAttempts() + 1);
            pendingLoginService.save(pending);
            throw invalidCode(username);
        }

        AuthUser user = authUserService.findByUsername(username)
                .orElseThrow(() -> invalidCode(username));

        pendingLoginService.deleteByUsername(username);
        authCookieService.addToken(response, jwtService.generateToken(user.getUsername()));

        log.info("Login verified: username={}", user.getUsername());
        return new MessageResponse("Login completed");
    }

    @Override
    public MessageResponse logout(HttpServletResponse response) {
        authCookieService.clear(response);
        return new MessageResponse("Logged out");
    }

    private AuthUser authenticate(String username, String password){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(username, password)
            );
            return (AuthUser) authentication.getPrincipal();
        }catch (AuthenticationException e) {
            log.warn("Login failed: username={}", safe(username));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    private ResponseStatusException invalidCode(String username){
        log.warn("Login verification failed: username={}", safe(username));
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired code");
    }

    private static String safe(String value){
        return value.replaceAll("[\\r\\n\\t]", "_");
    }
}
