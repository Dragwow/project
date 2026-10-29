package com.yumimi.authservice.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static String getCookieValue(HttpServletRequest request, String name){
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()){
            if (cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void addCookie(
            HttpServletResponse response,
            String name,
            String value,
            int maxAgeInSeconds
    ){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(maxAgeInSeconds);
        response.addCookie(cookie);

    }

    public static void deleteCookie(HttpServletResponse response, String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}