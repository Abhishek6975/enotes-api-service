package com.koyta.service.impl;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.koyta.dto.EmailRequest;
import com.koyta.exception.EmailSendingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailService {

	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String mailFrom;

	public EmailService(JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}

	public void sendEmail(EmailRequest emailReq) {

		try {

			MimeMessage message = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(mailFrom, emailReq.getTitle());
			helper.setTo(emailReq.getTo());
			helper.setSubject(emailReq.getSubject());
			helper.setText(emailReq.getMessage(), true);

			javaMailSender.send(message);
		} catch (MessagingException | MailException | UnsupportedEncodingException e) {
			// Log and wrap in your own exception or handle as needed
			log.error("Failed to send email", e);
			throw new EmailSendingException("Failed to send email: " + e.getMessage(), e);
		}

	}

}
