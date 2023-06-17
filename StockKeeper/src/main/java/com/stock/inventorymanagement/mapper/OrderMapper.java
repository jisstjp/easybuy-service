package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Order;
import com.stock.inventorymanagement.dto.OrderDto;

@Component
public class OrderMapper {
    @Autowired
    private ModelMapper modelMapper;

    public OrderMapper(ModelMapper modelMapper) {
	this.modelMapper = modelMapper;
    }

    public OrderDto toDto(Order order) {
	return modelMapper.map(order, OrderDto.class);
    }

}
