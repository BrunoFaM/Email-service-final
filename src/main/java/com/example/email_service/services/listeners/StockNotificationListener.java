package com.example.email_service.services.listeners;


import com.example.email_service.dtos.LowStockRequest;
import com.example.email_service.exceptions.EmailSendingException;
import com.example.email_service.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

@Service
public class StockNotificationListener {

    private static final Logger logger = Logger.getLogger(StockNotificationListener.class.getName());

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "lowStockQueue")
    public void handleStockNotification(LowStockRequest event) {
        logger.info("Received stock reduction event: " + event.name()+ " - New Stock: " + event.stock());

        String subject = "Notice, low stock of the product:: " + event.name();
        String message = "Only " + event.stock() + " units left of the product with ID: " + event.productId();

        try {
            emailService.sendEmailToAdmins(subject, message);
            logger.info("Email successfully sent to administrators.");
        } catch (Exception e) {
            throw new EmailSendingException("ERROR sending stock reduction email for: " + event.name(), e);
        }
    }
}
