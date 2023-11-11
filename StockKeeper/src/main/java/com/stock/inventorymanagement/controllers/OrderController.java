package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.OrderDto;
import com.stock.inventorymanagement.dto.OrderEmailRequestDto;
import com.stock.inventorymanagement.service.OrderService;
import com.stock.inventorymanagement.service.impl.PdfGenerationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/users/{userId}/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PdfGenerationServiceImpl pdfGenerationService;

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


    @GetMapping("/{orderId}/pdf")
    public ResponseEntity<ByteArrayResource> getOrderPdf(@PathVariable Long userId, @PathVariable Long orderId) {
        try {
            byte[] pdfContent = pdfGenerationService.generateOrderSummaryPdf(orderId);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order-summary-" + orderId + ".pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfContent.length)
                    .body(resource);
        } catch (Exception e) {
            // Log the exception details (e.g., using a logger)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating PDF", e);
        }
    }

    @PostMapping("/send-summary-email")
    public ResponseEntity<String> sendOrderSummaryEmail(@RequestBody OrderEmailRequestDto orderEmailRequestDto) {
        try {
             orderService.generateAndSendOrderPdf(orderEmailRequestDto.getOrderId(), orderEmailRequestDto.getEmail());
            return ResponseEntity.ok("Order summary email sent successfully to " + orderEmailRequestDto.getEmail());
        } catch (Exception e) {
            // Log the exception and return an appropriate error response
            return ResponseEntity.internalServerError().body("Failed to send order summary email: " + e.getMessage());
        }
    }

}
