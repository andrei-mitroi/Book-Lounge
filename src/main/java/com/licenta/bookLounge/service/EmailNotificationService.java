package com.licenta.bookLounge.service;

import com.licenta.bookLounge.BookLoungeApplication;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailNotificationService implements EmailSender{

    private static final Logger logger = LoggerFactory.getLogger(BookLoungeApplication.class);

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String receiver, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(email, true);
            mimeMessageHelper.setTo(receiver);
            mimeMessageHelper.setSubject("Welcome to BookLounge!");
            mimeMessageHelper.setFrom("hello@BookLounge.com");
            mailSender.send(mimeMessage);
        } catch (jakarta.mail.MessagingException e) {
            logger.error("Failed to send email", e);
            throw new RuntimeException(e);
        }
    }
}
