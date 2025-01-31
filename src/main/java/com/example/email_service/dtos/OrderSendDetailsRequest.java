package com.example.email_service.dtos;

import java.util.List;
//This is mine
public record OrderSendDetailsRequest(String email, List<OrderItemDtoRequest> products) {
}
