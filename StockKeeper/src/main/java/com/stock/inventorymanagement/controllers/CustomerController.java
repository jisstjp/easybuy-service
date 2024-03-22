package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.CustomerDto;
import com.stock.inventorymanagement.dto.CustomerInfoDto;
import com.stock.inventorymanagement.dto.CustomerSearchCriteria;
import com.stock.inventorymanagement.dto.StoreCreditUpdateRequest;
import com.stock.inventorymanagement.exception.ErrorResponse;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController extends BaseController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") Long id) {
        try {
            CustomerDto customerDto = customerService.getCustomerById(id);
            return ResponseEntity.ok(customerDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerDto customerDto) {
        try {
            customerService.registerCustomer(customerDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse("Something went wrong", "Please contact the support team.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(HttpServletRequest request, @PathVariable("id") Long id,
                                            @RequestBody CustomerDto customerDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!hasAnyRole(request, authentication,"ROLE_ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            Long userId = getUserIdFromToken(request);
            CustomerDto updatedCustomer = customerService.updateCustomer(id, customerDto, userId);
            return ResponseEntity.ok(updatedCustomer);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<Page<CustomerDto>> searchCustomers(HttpServletRequest request,@RequestBody CustomerSearchCriteria searchCriteria,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!hasAnyRole(request, authentication,"ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<CustomerDto> customerPage = customerService.searchCustomers(searchCriteria, page, size);
        return ResponseEntity.ok(customerPage);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCustomerByUserId(HttpServletRequest request,@PathVariable("userId") Long userId) {
        try {
            CustomerDto customerDto = customerService.getCustomerByUserId(userId);
            return ResponseEntity.ok(mapCustomerToCustomerInfoDto(customerDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{customerId}/store-credit")
    public ResponseEntity<?> updateStoreCredit(HttpServletRequest request,@PathVariable Long customerId,@RequestBody StoreCreditUpdateRequest storeCreditUpdateRequest) {
        try {
            Long userId = getUserIdFromToken(request);
            CustomerDto updatedCustomer = customerService.updateStoreCredit(customerId, storeCreditUpdateRequest, userId);
            return ResponseEntity.ok(mapCustomerToCustomerInfoDto(updatedCustomer));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private CustomerInfoDto mapCustomerToCustomerInfoDto(CustomerDto customer) {
        CustomerInfoDto customerInfoDto = new CustomerInfoDto();
        customerInfoDto.setFirstName(customer.getFirstName());
        customerInfoDto.setLastName(customer.getLastName());
        customerInfoDto.setAddressLine1(customer.getAddressLine1());
        customerInfoDto.setAddressLine2(customer.getAddressLine2());
        customerInfoDto.setCity(customer.getCity());
        customerInfoDto.setStateProvince(customer.getStateProvince());
        customerInfoDto.setZipPostalCode(customer.getZipPostalCode());
        customerInfoDto.setCountry(customer.getCountry());
        customerInfoDto.setEmail(customer.getEmail());
        customerInfoDto.setCompany(customer.getCompany());
        customerInfoDto.setStoreCredit(customer.getStoreCredit());
        // Handle nullable fields
        if (customer.getPhone() != null) {
            customerInfoDto.setPhone(customer.getPhone());
        }

        return customerInfoDto;
    }

}
