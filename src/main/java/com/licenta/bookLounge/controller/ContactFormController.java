package com.licenta.bookLounge.controller;

import com.licenta.bookLounge.model.ContactForm;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("BookLounge/v1")
public class ContactFormController {

	private final JavaMailSender emailSender;

	@PostMapping("/contact")
	public String sendEmail(@RequestBody ContactForm contactForm) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("booklounge.business@gmail.com");
		message.setSubject("A user has reached out");
		message.setText("Name: " + contactForm.getName() + "\n"
				+ "Email: " + contactForm.getEmail() + "\n"
				+ "Message: " + contactForm.getMessage());

		try {
			emailSender.send(message);
			return "Email sent successfully!";
		} catch (MailException e) {
			return "Failed to send email: " + e.getMessage();
		}
	}
}
