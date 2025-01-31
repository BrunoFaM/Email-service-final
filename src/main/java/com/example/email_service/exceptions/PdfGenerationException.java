package com.example.email_service.exceptions;

public class PdfGenerationException extends RuntimeException {
    public PdfGenerationException(String message, Throwable cause) {
        super(message);
    }
}
