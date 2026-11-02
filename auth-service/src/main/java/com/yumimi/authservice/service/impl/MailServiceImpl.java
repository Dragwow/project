package com.yumimi.authservice.service.impl;

import com.yumimi.authservice.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {


    private final String from;

    private final JavaMailSender sender;

    public MailServiceImpl(
            @Value("${spring.mail.username}") String email,
            JavaMailSender javaMailSender){
        this.from = email;
        this.sender = javaMailSender;
    }

    @Override
    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        sender.send(message);
    }
}
