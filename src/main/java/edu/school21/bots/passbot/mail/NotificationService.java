package edu.school21.bots.passbot.mail;

import edu.school21.bots.passbot.kernel.service.OrderService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {
	private final OrderService orderService;
	private final JavaMailSender emailSender;
//	@Value("${spring.email.properties.subject}")
	private String emailSubj="New_registration";
	private String emailFrom = "mikhail.malev@gmail.com";
	private String emailTo = "mikhail.malev@gmail.com";
	
	public NotificationService(OrderService orderService, JavaMailSender javaMailSender) {
		this.emailSender = javaMailSender;
		this.orderService = orderService;
	}
	public void sendEmail() {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(emailTo);
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setSubject(emailSubj);
		simpleMailMessage.setText("Hello");
		emailSender.send(simpleMailMessage);
		System.out.println("Sender message");
	}
}
