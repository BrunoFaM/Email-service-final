package com.example.email_service.services.listeners;

import com.example.email_service.dtos.UserDtoOutput;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserRegisteredListener {

    private static final Logger logger = Logger.getLogger(UserRegisteredListener.class.getName());

    private final JavaMailSender mailSender;

    public UserRegisteredListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = "userQueue")
    public void handleUserRegisteredEvent(UserDtoOutput user) {
        if (user == null || user.getEmail() == null) {
            logger.warning("Event received with invalid user data. Discarding event.");
            return;
        }

        logger.info("User registration event received: " + user.getEmail());

        try {
            sendWelcomeEmail(user.getEmail(), user.getUsername());
            logger.info("Welcome email successfully sent to: " + user.getEmail());
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "ERROR sending welcome email to " + user.getEmail() + ": " + e.getMessage(), e);
        }
    }

    private void sendWelcomeEmail(String email, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Welcome to our service! :)))");
        helper.setText("<h1>Hi " + name + "!</h1><p>Thank you for registering :D. We are very happy to see you here.</p>", true);

        mailSender.send(message);

        System.out.println("Email sent to: " + email);
    }
}
