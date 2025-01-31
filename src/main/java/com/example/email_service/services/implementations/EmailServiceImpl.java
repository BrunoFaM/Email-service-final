package com.example.email_service.services.implementations;

import com.example.email_service.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.admins}")
    private List<String> adminEmails;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendOrderEmail(String email, byte[] pdfContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Confirmation of your order");
        helper.setText("Attached you will find the details of your order. Thank you for choosing us.", true);
        helper.addAttachment("order.pdf", () -> new ByteArrayInputStream(pdfContent));

        mailSender.send(message);
        System.out.println("Email sent to: " + email);

    }

    @Override
    public void sendEmailToAdmins(String subject, String message) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setFrom(senderEmail);
            helper.setTo(adminEmails.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(message);

            mailSender.send(mail);
            System.out.println("✅ Email enviado a los administradores.");
        } catch (Exception e) {
            System.err.println("❌ Error enviando email: " + e.getMessage());
        }
    }
}
