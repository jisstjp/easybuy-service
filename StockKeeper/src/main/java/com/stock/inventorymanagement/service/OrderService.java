package com.stock.inventorymanagement.service;

import com.itextpdf.text.DocumentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.stock.inventorymanagement.dto.OrderDto;
import com.stock.inventorymanagement.dto.OrderSearchCriteria;

import javax.mail.MessagingException;
import java.io.IOException;

public interface OrderService {

    OrderDto createOrder(Long userId, OrderDto orderDto);

    OrderDto getOrder(Long userId, Long orderId);

    void cancelOrder(Long userId, Long orderId);

    Page<OrderDto> getOrdersByUserId(Long userId, int page, int size);

    Page<OrderDto> getOrdersByUserId(Long userId, Pageable pageable);


    Page<OrderDto> searchOrders(OrderSearchCriteria searchCriteria, Pageable pageable);

    OrderDto updateOrder(Long orderId, OrderDto orderDto);

    OrderDto getOrder(Long orderId);

     void generateAndSendOrderPdf(Long orderId, String recipientEmail,Long userId,boolean isAdminOrManager) throws MessagingException, DocumentException, IOException;


    }
