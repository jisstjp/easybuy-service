package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.domain.Customer;
import com.stock.inventorymanagement.dto.StoreCreditUpdateRequest;
import org.springframework.data.domain.Page;

import com.stock.inventorymanagement.dto.CustomerDto;
import com.stock.inventorymanagement.dto.CustomerSearchCriteria;

import java.math.BigDecimal;
import java.util.Optional;

public interface CustomerService {

    CustomerDto registerCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(Long customerId, CustomerDto customerDto,Long userId);

    CustomerDto getCustomerById(Long customerId);

    Page<CustomerDto> searchCustomers( CustomerSearchCriteria searchCriteria,int page, int size);


    public CustomerDto getCustomerByUserId(Long userId);

    CustomerDto updateStoreCredit(Long customerId, StoreCreditUpdateRequest storeCreditUpdateRequest, Long userId);

    public BigDecimal getAvailableStoreCredit(Long userId);

}
