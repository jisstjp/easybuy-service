package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Payment;
import com.stock.inventorymanagement.dto.PaymentDto;

@Component
public class PaymentMapper {
    @Autowired
    private ModelMapper modelMapper;

    public PaymentDto toDto(Payment payment) {
	return modelMapper.map(payment, PaymentDto.class);
    }

    public Payment toEntity(PaymentDto paymentDto) {
	return modelMapper.map(paymentDto, Payment.class);
    }

}
