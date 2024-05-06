package com.stock.inventorymanagement.service.impl;

import com.stock.inventorymanagement.domain.Customer;
import com.stock.inventorymanagement.domain.CustomerSalesPerson;
import com.stock.inventorymanagement.domain.SalesPerson;
import com.stock.inventorymanagement.dto.CustomerSalesPersonDTO;
import com.stock.inventorymanagement.repository.CustomerRepository;
import com.stock.inventorymanagement.repository.CustomerSalesPersonRepository;
import com.stock.inventorymanagement.repository.SalesPersonRepository;
import com.stock.inventorymanagement.service.CustomerSalesPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerSalesPersonServiceImpl implements CustomerSalesPersonService {

    @Autowired
    private CustomerSalesPersonRepository customerSalesPersonRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SalesPersonRepository salesPersonRepository;

    @Transactional
    @Override
    public CustomerSalesPersonDTO createRelationship(CustomerSalesPersonDTO relationshipDto) {
        return createRelationships(relationshipDto, false).get(0);
    }

    @Transactional
    @Override
    public List<CustomerSalesPersonDTO> createMultipleRelationships(CustomerSalesPersonDTO relationshipDto) {
        return createRelationships(relationshipDto, true);
    }

    private List<CustomerSalesPersonDTO> createRelationships(CustomerSalesPersonDTO relationshipDto, boolean multiple) {
        SalesPerson salesPerson = salesPersonRepository.findById(relationshipDto.getSalesPersonId())
                .orElseThrow(() -> new IllegalArgumentException("SalesPerson not found with ID: " + relationshipDto.getSalesPersonId()));

        List<CustomerSalesPersonDTO> createdRelationships = new ArrayList<>();
        for (Long customerId : relationshipDto.getCustomerIds()) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

            // Check if a relationship already exists
            boolean relationshipExists = customerSalesPersonRepository.existsByCustomerAndSalesPerson(customer, salesPerson);
            if (relationshipExists) {
                throw new IllegalArgumentException("Relationship already exists between Customer ID: " + customerId + " and SalesPerson ID: " + relationshipDto.getSalesPersonId());
            }

            CustomerSalesPerson relationship = new CustomerSalesPerson();
            relationship.setCustomer(customer);
            relationship.setSalesPerson(salesPerson);
            relationship.setAssignedDate(relationshipDto.getAssignedDate());
            customerSalesPersonRepository.save(relationship);

            createdRelationships.add(convertToDto(relationship));
        }
        return createdRelationships;
    }

    @Transactional
    @Override
    public void deleteRelationship(Long id) {
        CustomerSalesPerson relationship = customerSalesPersonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Relationship not found with ID: " + id));
        customerSalesPersonRepository.delete(relationship);
    }

    @Override
    public List<CustomerSalesPersonDTO> findAllRelationshipsByCustomerId(Long customerId) {
        return customerSalesPersonRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerSalesPersonDTO> findAllRelationshipsBySalesPersonId(Long salesPersonId) {
        return customerSalesPersonRepository.findBySalesPersonId(salesPersonId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CustomerSalesPersonDTO convertToDto(CustomerSalesPerson relationship) {
        return new CustomerSalesPersonDTO(
                relationship.getId(),
                Collections.singletonList(relationship.getCustomer().getId()),
                relationship.getSalesPerson().getId(),
                relationship.getAssignedDate()
        );
    }

    public Map<Long, Long> findCustomerIdsBySalesPersonId(Long salesPersonId) {
        List<CustomerSalesPerson> relationships = customerSalesPersonRepository.findBySalesPersonId(salesPersonId);
        return relationships.stream()
                .filter(relationship -> relationship.getCustomer().getUser() != null)
                .collect(Collectors.toMap(
                        relationship -> relationship.getCustomer().getId(),
                        relationship -> relationship.getCustomer().getUser().getId()
                ));




    }

    @Transactional
    @Override
    public  List<CustomerSalesPersonDTO> createOrUpdateRelationships(CustomerSalesPersonDTO relationshipDto) {
        SalesPerson salesPerson = salesPersonRepository.findById(relationshipDto.getSalesPersonId())
                .orElseThrow(() -> new IllegalArgumentException("SalesPerson not found with ID: " + relationshipDto.getSalesPersonId()));

        // Remove existing relationships for the SalesPerson
        customerSalesPersonRepository.deleteAllBySalesPerson(salesPerson);

        List<CustomerSalesPersonDTO> createdRelationships = new ArrayList<>();
        for (Long customerId : relationshipDto.getCustomerIds()) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

            CustomerSalesPerson relationship = new CustomerSalesPerson();
            relationship.setCustomer(customer);
            relationship.setSalesPerson(salesPerson);
            relationship.setAssignedDate(relationshipDto.getAssignedDate());
            customerSalesPersonRepository.save(relationship);

            createdRelationships.add(convertToDto(relationship));
        }

        return createdRelationships;
    }



}
