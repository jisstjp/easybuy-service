package com.stock.inventorymanagement.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stock.inventorymanagement.dto.CustomerDto;
import com.stock.inventorymanagement.dto.CustomerSearchCriteria;
import com.stock.inventorymanagement.exception.ErrorResponse;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.service.CustomerService;

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
	    ErrorResponse errorResponse = new ErrorResponse("Something went wrong","Please contact the support team.");
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(HttpServletRequest request, @PathVariable("id") Long id,
	    @RequestBody CustomerDto customerDto) {
	try {
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
    public ResponseEntity<Page<CustomerDto>> searchCustomers(@RequestBody CustomerSearchCriteria searchCriteria,
	    @RequestParam(value = "page", defaultValue = "0") int page,
	    @RequestParam(value = "size", defaultValue = "10") int size) {
	Page<CustomerDto> customerPage = customerService.searchCustomers(searchCriteria, page, size);
	return ResponseEntity.ok(customerPage);
    }

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getCustomerByUserId(@PathVariable("userId") Long userId) {
		try {
			CustomerDto customerDto = customerService.getCustomerByUserId(userId);
			return ResponseEntity.ok(customerDto);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception ex) {
			ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

}
