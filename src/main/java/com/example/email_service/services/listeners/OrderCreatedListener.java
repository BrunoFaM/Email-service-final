package com.example.email_service.services.listeners;

import com.example.email_service.dtos.OrderSendDetailsRequest;
import com.example.email_service.exceptions.EmailSendingException;
import com.example.email_service.exceptions.PdfGenerationException;
import com.example.email_service.services.EmailService;

import com.example.email_service.services.PdfGeneratorService;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class OrderCreatedListener {

    private static final Logger logger = Logger.getLogger(OrderCreatedListener.class.getName());

    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;


    @RabbitListener(queues = "orderDetailsQueue", errorHandler = "rabbitMqExceptionHandler")
    public void handleOrderCreatedEvent(OrderSendDetailsRequest order) {

        logger.info("Order received");
        logger.info("Sending email to: " + order.email());

        try {
            byte[] pdfContent = pdfGeneratorService.generateOrderPdf(order.email(), order.products());
            emailService.sendOrderEmail(order.email(), pdfContent);
        } catch (IOException e) {
            throw new PdfGenerationException("ERROR generating PDF for user: " + order.email(), e);
        } catch (MessagingException e) {
            throw new EmailSendingException("ERROR sending email to " + order.email(), e);
        }
    }

}
