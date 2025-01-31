package com.example.email_service.services.listeners;


import com.example.email_service.dtos.LowStockRequest;
import com.example.email_service.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockNotificationListener {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "lowStockQueue")
    public void handleStockNotification(LowStockRequest event) {
        System.out.println("Recibido evento de stock reducido: " + event.name() + " - Nuevo Stock: " + event.stock());

        String subject = "Aviso, poco stock del producto: " + event.name();
        String message = "Solo quedan " + event.stock() + " unidades del producto con ID: " + event.productId() ;

        emailService.sendEmailToAdmins(subject, message);
    }
}
