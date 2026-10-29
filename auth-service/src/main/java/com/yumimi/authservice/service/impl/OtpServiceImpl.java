package com.yumimi.authservice.service.impl;

import com.yumimi.authservice.entity.OtpEntry;
import com.yumimi.authservice.repository.OtpEntryRepository;
import com.yumimi.authservice.service.MailService;
import com.yumimi.authservice.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpEntryRepository repository;
    private final MailService service;

    @Override
    @CacheEvict(value = "otpCache", key = "#email")
    public void sendOtp(String email) {
        String code = String.valueOf(new Random().nextInt(900000)+99999);
        repository.save(OtpEntry.builder()
                .email(email)
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build());
        service.sendMessage(email, "Code", "You`re code" + code);
    }

    @Override
    @CachePut(value = "otpCache", key = "#email")
    public void removeOtp(String email) {}

    @Override
    @Transactional
    public boolean verifyOtp(String email, String code) {
        Optional<OtpEntry> otpEntry = repository.findTopByEmailAndUsedFalseOrderByCreatedAtDesc(email);
        if (otpEntry.isEmpty()) return false;

        OtpEntry otp = otpEntry.get();
        if (!otp.getCode().equals(code)) return false;
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) return false;

        otp.setUsed(true);
        repository.save(otp);
        return true;

    }
}
