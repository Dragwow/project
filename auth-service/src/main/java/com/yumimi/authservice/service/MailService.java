package com.yumimi.authservice.service;

public interface MailService {

    void sendMessage(String to, String subject, String text);
}
