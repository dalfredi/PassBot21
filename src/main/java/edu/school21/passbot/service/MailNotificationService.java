package edu.school21.passbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailNotificationService {
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;
    private String emailTo;

    public MailNotificationService(JavaMailSender javaMailSender) {
        this.emailSender = javaMailSender;
    }

    public void sendEmail(String text, String email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(emailFrom);
        simpleMailMessage.setSubject("Order status update");
        simpleMailMessage.setText(text);
        emailSender.send(simpleMailMessage);
    }
}
