package com.stock.inventorymanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.inventorymanagement.dto.OrderDto;
import com.stock.inventorymanagement.service.OrderService;

@RestController
@RequestMapping("/api/v1/users/{userId}/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@PathVariable Long userId, @RequestBody OrderDto orderDto) {
	OrderDto createdOrder = orderService.createOrder(userId, orderDto);
	return ResponseEntity.ok(createdOrder);
    }

}
