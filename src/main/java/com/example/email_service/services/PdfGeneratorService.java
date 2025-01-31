package com.example.email_service.services;



import com.example.email_service.dtos.OrderItemDtoRequest;

import java.io.IOException;
import java.util.List;

public interface PdfGeneratorService {

    byte[] generateOrderPdf(String email, List<OrderItemDtoRequest> orderItems) throws IOException;
}
