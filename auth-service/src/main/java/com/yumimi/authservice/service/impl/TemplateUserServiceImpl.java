package com.yumimi.authservice.service.impl;

import com.yumimi.authservice.dto.request.AuthRequest;
import com.yumimi.authservice.dto.request.RegistrationRequest;
import com.yumimi.authservice.dto.response.AuthResponse;
import com.yumimi.authservice.service.TemplateUserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TemplateUserServiceImpl implements TemplateUserService {


    @Override
    @CachePut(value = "tempUser", key = "#request.email")
    public RegistrationRequest saveTempUser(RegistrationRequest request) {
        return request;
    }

    @Override
    @CachePut(value = "tempUser", key = "#email")
    public AuthRequest saveTempUserByLogin(String email, AuthRequest request) {
        return request;
    }

    @Override
    @CachePut(value = "tempUser", key = "#email")
    public AuthResponse saveTempEmail(String email) {
        return new AuthResponse(email);
    }

    @Override
    @Cacheable(value = "tempUser", key = "#email")
    public RegistrationRequest getTempUserByEmail(String email) {
        return null;
    }

    @Override
    @Cacheable(value = "tempUser", key = "#email")
    public AuthRequest getTempUserByLogin(String email) {
        return null;
    }

    @Override
    @CacheEvict(value = "tempUser", key = "#email")
    public void deleteTempUser(String email) {

    }
}
