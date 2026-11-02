package com.yumimi.authservice.service;

public interface OtpService {

    String generateCode();

    String hash(String hash);

    boolean matches(String code, String hash);

}
