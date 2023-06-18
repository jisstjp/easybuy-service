package com.stock.inventorymanagement.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.inventorymanagement.domain.Order;
import com.stock.inventorymanagement.domain.Payment;
import com.stock.inventorymanagement.dto.OrderDto;
import com.stock.inventorymanagement.dto.PaymentDto;
import com.stock.inventorymanagement.mapper.PaymentMapper;
import com.stock.inventorymanagement.repository.PaymentRepository;
import com.stock.inventorymanagement.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentDto processPayment(Order order, OrderDto orderDto) {

	// Call the payment gateway API to process the payment

	Payment payment = new Payment();
	payment.setOrderId(order.getId());
	payment.setAmount(order.getTotalPrice());
	//payment.setPaymentMethod(orderDto.getPayment().getPaymentMethod());
	payment.setStatus(Optional.ofNullable(orderDto.getPayment().getStatus()).orElse("Pending"));
	payment.setPaymentMethod(Optional.ofNullable(orderDto.getPayment().getPaymentMethod()).orElse("Cash"));
	payment.setCreatedAt(LocalDateTime.now());
	payment.setUpdatedAt(LocalDateTime.now());
	payment.setDeleted(false);
	Payment savedPayment = paymentRepository.save(payment);

	return paymentMapper.toDto(savedPayment);
    }

}
