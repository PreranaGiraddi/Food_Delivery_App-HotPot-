package com.hotpot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	// ✅ Send order confirmation email
	public void sendOrderConfirmationEmail(String toEmail, String userName, Long orderId) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("HotPot - Order Confirmed! 🍲");
		message.setText("Hi " + userName + ",\n\n" + "Your order #" + orderId + " has been placed successfully!\n"
				+ "We will notify you once your order is dispatched.\n\n" + "Thank you for ordering from HotPot!\n"
				+ "Team HotPot 🍲");
		mailSender.send(message);
	}

	// ✅ Send order status update email
	public void sendOrderStatusEmail(String toEmail, String userName, Long orderId, String status) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("HotPot - Order Status Update 🍲");
		message.setText("Hi " + userName + ",\n\n" + "Your order #" + orderId + " status has been updated to: " + status
				+ "\n\n" + "Thank you for ordering from HotPot!\n" + "Team HotPot 🍲");
		mailSender.send(message);
	}
}