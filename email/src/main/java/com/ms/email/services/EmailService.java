package com.ms.email.services;

import com.ms.email.dtos.EmailRecordDto;
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

    public void sendEmail(EmailRecordDto emailModel) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailModel.emailTo());
        message.setSubject(emailModel.subject());
        message.setText(emailModel.text());
        emailSender.send(message);
    }

}
