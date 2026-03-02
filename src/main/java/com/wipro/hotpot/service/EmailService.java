package com.wipro.hotpot.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendOrderConfirmationEmail(String toEmail,
                                           String userName,
                                           Long orderId) {
        System.out.println("==========================================");
        System.out.println("📧 EMAIL SIMULATION - Order Confirmation");
        System.out.println("To      : " + toEmail);
        System.out.println("Subject : HotPot - Order Confirmed!");
        System.out.println("Message : Hi " + userName +
                           ", your order #" + orderId +
                           " placed successfully!");
        System.out.println("==========================================");
    }

    public void sendOrderStatusEmail(String toEmail,
                                     String userName,
                                     Long orderId,
                                     String status) {
        System.out.println("==========================================");
        System.out.println("📧 EMAIL SIMULATION - Status Update");
        System.out.println("To      : " + toEmail);
        System.out.println("Subject : HotPot - Order Status Update");
        System.out.println("Message : Hi " + userName +
                           ", your order #" + orderId +
                           " status: " + status);
        System.out.println("==========================================");
    }
}