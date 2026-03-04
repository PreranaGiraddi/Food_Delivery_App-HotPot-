package com.wipro.hotpot.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    

    public void sendOrderConfirmationEmail(String toEmail, 
                                           String userName, 
                                           Long orderId) {
     
        System.out.println("📧 [EMAIL SIMULATION] Order Confirmation sent to: " 
                          + toEmail);
        System.out.println("   Hi " + userName + 
                          ", your order #" + orderId + 
                          " is confirmed!");
    }

    public void sendOrderStatusEmail(String toEmail, 
                                     String userName,
                                     Long orderId, 
                                     String status) {
      
        System.out.println("📧 [EMAIL SIMULATION] Status Update sent to: " 
                          + toEmail);
        System.out.println("   Hi " + userName + 
                          ", your order #" + orderId + 
                          " status is: " + status);
    }
}