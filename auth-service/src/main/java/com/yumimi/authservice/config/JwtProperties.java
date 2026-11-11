package com.yumimi.authservice.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        @NotBlank String privateKeyPath,
        @NotBlank String publicKeyPath,
        @Positive long expirationMs,
        @DefaultValue("auth-service") String issuer
) {}
