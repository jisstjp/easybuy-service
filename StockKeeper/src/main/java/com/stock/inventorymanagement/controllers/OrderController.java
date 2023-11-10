package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.OrderDto;
import com.stock.inventorymanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        OrderDto orderDto = orderService.getOrder(userId, orderId);
        return ResponseEntity.ok(orderDto);
    }


    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        orderService.cancelOrder(userId, orderId);
        return ResponseEntity.ok("Order cancelled successfully.");
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {
        //Page<OrderDto> orders = orderService.getOrdersByUserId(userId, page, size);

        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<OrderDto> orders = orderService.getOrdersByUserId(userId, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(orders);
    }


}
