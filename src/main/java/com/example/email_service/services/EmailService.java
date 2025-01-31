package com.example.email_service.services;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendOrderEmail(String email, byte[] pdfContent) throws MessagingException;

    void sendEmailToAdmins(String subject, String message);
}

