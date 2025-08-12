package com.rh.email.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    public void sendEmail(com.rh.email.dtos.EmailRecordDto emailModel) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailModel.emailTo());
        message.setSubject(emailModel.subject());
        message.setText(emailModel.text());
        emailSender.send(message);
    }

}
