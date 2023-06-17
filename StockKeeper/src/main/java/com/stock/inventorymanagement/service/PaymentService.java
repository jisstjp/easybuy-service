package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.domain.Order;
import com.stock.inventorymanagement.dto.OrderDto;
import com.stock.inventorymanagement.dto.PaymentDto;

public interface PaymentService {
    
    PaymentDto processPayment(Order order, OrderDto orderDto);

}
