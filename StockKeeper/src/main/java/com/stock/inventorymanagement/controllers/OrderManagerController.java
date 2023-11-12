package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.OrderDto;
import com.stock.inventorymanagement.dto.OrderSearchCriteria;
import com.stock.inventorymanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderManagerController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/search")
    public ResponseEntity<Page<OrderDto>> searchOrders(@RequestBody OrderSearchCriteria searchCriteria,
                                                       @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String order
    ) {
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orderPage = orderService.searchOrders(searchCriteria, PageRequest.of(page, size, sort));
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
