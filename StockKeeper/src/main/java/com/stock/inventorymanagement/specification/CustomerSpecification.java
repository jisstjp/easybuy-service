package com.stock.inventorymanagement.specification;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Customer;

@Component
public class CustomerSpecification {

    public Specification<Customer> searchCustomers(String field, String value, String matchType) {
	return (root, criteriaQuery, criteriaBuilder) -> {
	    Path<String> fieldPath = root.get(field);
	    String searchValue = "%" + value.toLowerCase() + "%";
	    if (matchType.equalsIgnoreCase("exact")) {
		return criteriaBuilder.equal(criteriaBuilder.lower(fieldPath), value.toLowerCase());
	    } else {
		return criteriaBuilder.like(criteriaBuilder.lower(fieldPath), searchValue);
	    }
	};
    }

}
