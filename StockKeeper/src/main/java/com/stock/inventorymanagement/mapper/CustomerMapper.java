package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Customer;
import com.stock.inventorymanagement.dto.CustomerDto;

@Component
public class CustomerMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CustomerDto toDto(Customer customer) {
	return modelMapper.map(customer, CustomerDto.class);
    }

    public Customer toEntity(CustomerDto customerDto) {
	return modelMapper.map(customerDto, Customer.class);
    }

}
