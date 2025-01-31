package com.example.email_service.dtos;



import com.example.email_service.dtos.enums.OrderStatus;

import java.util.List;

public class OrderDtoOutput {

    private Long id;

    private Long userId;

    private OrderStatus orderStatus;

    private List<OrderItemDtoOutput> orderItems;

    private Double total;

    public OrderDtoOutput(Long id, Long userId, OrderStatus orderStatus, List<OrderItemDtoOutput> orderItems, Double total) {
        this.id = id;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderItemDtoOutput> getOrderItems() {
        return orderItems;
    }

    public Double getTotal() {
        return total;
    }
}
