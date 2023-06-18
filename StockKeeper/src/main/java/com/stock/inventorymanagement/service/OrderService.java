package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.dto.OrderDto;

public interface OrderService {

    OrderDto createOrder(Long userId, OrderDto orderDto);
    OrderDto getOrder(Long userId, Long orderId);
     void cancelOrder(Long userId, Long orderId);
    //OrderDto getOrder(Long orderId);


}
