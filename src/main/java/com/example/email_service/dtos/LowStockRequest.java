package com.example.email_service.dtos;

public record LowStockRequest(Long productId, String name, Integer stock) {
}
