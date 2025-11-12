package com.Library.UserService.services;

import com.Library.UserService.models.AuthUser;
import com.Library.UserService.util.EmailSendingException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${send.message.from}")
    private String from;

    public void send(AuthUser authUser, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(authUser.getEmail());
            message.setSubject("Password recovery");
            message.setText(textPrep(authUser.getUsername(), code));
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException(e.getMessage());
        }

    }

    private String textPrep(String username, String code) {
        return "Hello dear " + username + "Password recovery verification code: " + code +
                ". Do not share this code with anyone unless you requested a password recovery, simply ignore this message.";
    }
}
