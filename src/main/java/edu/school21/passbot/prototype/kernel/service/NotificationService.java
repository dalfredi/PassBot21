package edu.school21.passbot.prototype.kernel.service;

import edu.school21.passbot.prototype.kernel.models.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {
	private final OrderService orderService;
	private final JavaMailSender emailSender;
	private final String emailSubj="Order status";
	private final String emailFrom="mikhail.malev@gmail.com";
	private String emailTo;

	public NotificationService(OrderService orderService, JavaMailSender javaMailSender) {
		this.emailSender = javaMailSender;
		this.orderService = orderService;
	}

	public void sendEmail(String text, String email) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(email);
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setSubject(emailSubj);
		simpleMailMessage.setText(text);
		emailSender.send(simpleMailMessage);
	}
}
