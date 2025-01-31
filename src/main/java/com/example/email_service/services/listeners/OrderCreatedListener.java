package com.example.email_service.services.listeners;

import com.example.email_service.dtos.OrderSendDetailsRequest;
import com.example.email_service.services.EmailService;

import com.example.email_service.services.PdfGeneratorService;
import com.example.email_service.utils.UserServiceClient;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderCreatedListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private UserServiceClient userServiceClient;

    @RabbitListener(queues = "orderDetailsQueue")
    public void handleOrderCreatedEvent(OrderSendDetailsRequest order) {
        //System.out.println("Order received: " + order.getId());
        //System.out.println("Searching email's user with ID: " + order.getUserId());

        //String userEmail = userServiceClient.getUserEmail(order.getUserId());

        //if (userEmail == null) {
        //    System.err.println("ERROR: Can't obtain user's email");
        //    return;
        //}

        try {
            byte[] pdfContent = pdfGeneratorService.generateOrderPdf(order.email(), order.products());
            emailService.sendOrderEmail(order.email(), pdfContent);
        } catch (IOException | MessagingException e) {
            System.err.println("ERROR making PDF or sending it" + e.getMessage());
        }
    }

}
