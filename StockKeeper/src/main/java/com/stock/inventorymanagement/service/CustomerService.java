package com.stock.inventorymanagement.service;

import org.springframework.data.domain.Page;

import com.stock.inventorymanagement.dto.CustomerDto;
import com.stock.inventorymanagement.dto.CustomerSearchCriteria;

public interface CustomerService {

    CustomerDto registerCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(Long customerId, CustomerDto customerDto,Long userId);

    CustomerDto getCustomerById(Long customerId);

    Page<CustomerDto> searchCustomers( CustomerSearchCriteria searchCriteria,int page, int size);

}
