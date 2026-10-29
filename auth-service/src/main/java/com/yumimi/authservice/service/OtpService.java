package com.yumimi.authservice.service;

public interface OtpService {

    void sendOtp(String email);
    void removeOtp(String email);
    boolean verifyOtp(String email, String code);

}
