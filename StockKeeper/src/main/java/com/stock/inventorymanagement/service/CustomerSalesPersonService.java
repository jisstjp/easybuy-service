package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.domain.CustomerSalesPerson;
import com.stock.inventorymanagement.domain.SalesPerson;
import com.stock.inventorymanagement.dto.CustomerSalesPersonDTO;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;

public interface CustomerSalesPersonService {

    CustomerSalesPersonDTO createRelationship(CustomerSalesPersonDTO relationshipDto);

    void deleteRelationship(Long id);

    List<CustomerSalesPersonDTO> findAllRelationshipsByCustomerId(Long customerId);

    List<CustomerSalesPersonDTO> findAllRelationshipsBySalesPersonId(Long salesPersonId);
    public List<CustomerSalesPersonDTO> createMultipleRelationships(CustomerSalesPersonDTO relationshipDto);
    public Map<Long, Long> findCustomerIdsBySalesPersonId(Long salesPersonId);

    public List<CustomerSalesPersonDTO> createOrUpdateRelationships(CustomerSalesPersonDTO relationshipDto) ;


    }
