package com.stock.inventorymanagement.service.impl;

import com.stock.inventorymanagement.domain.Customer;
import com.stock.inventorymanagement.domain.Role;
import com.stock.inventorymanagement.domain.User;
import com.stock.inventorymanagement.dto.CustomerDto;
import com.stock.inventorymanagement.dto.CustomerSearchCriteria;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.CustomerMapper;
import com.stock.inventorymanagement.repository.CustomerRepository;
import com.stock.inventorymanagement.repository.RoleRepository;
import com.stock.inventorymanagement.repository.UserRepository;
import com.stock.inventorymanagement.service.CustomerService;
import com.stock.inventorymanagement.specification.CustomerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerSpecification customerSpecification;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerDto registerCustomer(CustomerDto customerDto) {
        String email = customerDto.getEmail();
        Customer existingCustomer = customerRepository.findByEmail(email);

        if (existingCustomer != null) {
            throw new IllegalStateException("Customer already exists with the provided email.");

        }

        Customer customer = customerMapper.toEntity(customerDto);
        customer.setApprovalStatus("P");
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto, Long userId) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

        Customer updatedCustomer = customerMapper.toEntity(customerDto);
        updatedCustomer.setId(existingCustomer.getId());
        String approvalStatus = customerDto.getApprovalStatus();
        if (approvalStatus != null && (approvalStatus.equals("A") || approvalStatus.equals("R"))) {
            // Perform the approval or rejection
            updatedCustomer.setApprovalStatus(approvalStatus);

            if (approvalStatus.equals("A")) {
                Optional<User> existingUser = userRepository.findByUsername(updatedCustomer.getEmail());
                User user = existingUser.orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(updatedCustomer.getEmail());
                    newUser.setPassword(customerDto.getPassword());
                    newUser.setEmail(updatedCustomer.getEmail());
                    newUser.setFirstName(customerDto.getFirstName());
                    newUser.setLastName(customerDto.getLastName());
                    Role role = roleRepository.findByName("ROLE_CUSTOMER");
                    newUser.getRoles().add(role);
                    return userRepository.save(newUser);
                });

                updatedCustomer.setUser(user);

            }


        }

        Customer savedCustomer = customerRepository.save(updatedCustomer);

        return customerMapper.toDto(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            return customerMapper.toDto(customer);
        }
        throw new ResourceNotFoundException("Customer", "id", customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> searchCustomers(CustomerSearchCriteria searchCriteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Customer> specification = customerSpecification.searchCustomers(searchCriteria.getField(),
                searchCriteria.getValue(), searchCriteria.getMatchType());
        Page<Customer> customerPage = customerRepository.findAll(specification, pageable);
        return customerPage.map(customerMapper::toDto);
    }

    public CustomerDto getCustomerByUserId(Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "user_id", userId));
        return customerMapper.toDto(customer);
    }

}
