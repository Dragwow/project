package com.yumimi.authservice.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class AuthCookieService {

    private final CookieProperties cookieProperties;
    private final JwtProperties jwtProperties;

    public void addToken(HttpServletResponse response, String token){
        ResponseCookie cookie = build(token)
                .maxAge(Duration.ofMillis(jwtProperties.expirationMs()))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    }

    public void clear(HttpServletResponse response){
        ResponseCookie cookie = build("")
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private ResponseCookie.ResponseCookieBuilder build(String value){
        return ResponseCookie.from(cookieProperties.name(), value)
                .httpOnly(true)
                .secure(cookieProperties.secure())
                .sameSite(cookieProperties.sameSite())
                .path(cookieProperties.path());
    }
}
