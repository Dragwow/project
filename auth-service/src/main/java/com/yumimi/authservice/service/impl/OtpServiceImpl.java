package com.yumimi.authservice.service.impl;

import com.yumimi.authservice.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final PasswordEncoder passwordEncoder;


    @Override
    public String generateCode() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }

    @Override
    public String hash(String code) {
        return passwordEncoder.encode(code);
    }

    @Override
    public boolean matches(String code, String hash) {
        return passwordEncoder.matches(code, hash);
    }
}
