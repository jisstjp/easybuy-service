package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.dto.OrderDto;

public interface OrderService {

    OrderDto createOrder(Long userId, OrderDto orderDto);
    
    //OrderDto getOrder(Long orderId);


}
