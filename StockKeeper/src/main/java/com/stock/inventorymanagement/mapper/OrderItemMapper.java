package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.OrderItem;
import com.stock.inventorymanagement.dto.OrderItemDto;

@Component
public class OrderItemMapper {

    @Autowired
    private ModelMapper modelMapper;

    public OrderItemMapper(ModelMapper modelMapper) {
	this.modelMapper = modelMapper;
    }

    public OrderItemDto toDto(OrderItem orderItem) {
	return modelMapper.map(orderItem, OrderItemDto.class);
    }
}
