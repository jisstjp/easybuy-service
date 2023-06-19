package com.stock.inventorymanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stock.inventorymanagement.dto.OrderDto;
import com.stock.inventorymanagement.dto.OrderSearchCriteria;
import com.stock.inventorymanagement.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderManagerController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/search")
    public ResponseEntity<Page<OrderDto>> searchOrders(@RequestBody OrderSearchCriteria searchCriteria,
	    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
	Pageable pageable = PageRequest.of(page, size);
	Page<OrderDto> orderPage = orderService.searchOrders(searchCriteria, pageable);
	return ResponseEntity.ok(orderPage);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long orderId, @RequestBody OrderDto orderDto) {
	OrderDto updatedOrder = orderService.updateOrder(orderId, orderDto);
	return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) {
	OrderDto orderDto = orderService.getOrder(orderId);
	return ResponseEntity.ok(orderDto);
    }

}
