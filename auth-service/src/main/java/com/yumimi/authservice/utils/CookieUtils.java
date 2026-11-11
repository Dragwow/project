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
}