package com.siddh.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendVerificationMail(String email,String token){
        String verifyUrl="http://localhost:8080/api/v1/auth/verify?token=" + token;

        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify your email");
        message.setText("Click the link to verify your account: " + verifyUrl);

        javaMailSender.send(message);
    }
}
