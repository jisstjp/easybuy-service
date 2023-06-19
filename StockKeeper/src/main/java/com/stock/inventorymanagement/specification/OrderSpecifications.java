package com.stock.inventorymanagement.specification;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Order;

@Component
public class OrderSpecifications {

    public Specification<Order> buildSpecification(String field, String value, String matchType) {
	return (root, query, criteriaBuilder) -> {
	    Predicate predicate;
	    if (matchType.equalsIgnoreCase("equal")) {
		predicate = criteriaBuilder.equal(criteriaBuilder.lower(root.get(field)), value.toLowerCase());
	    } else if (matchType.equalsIgnoreCase("like")) {
		predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(field)),
			"%" + value.toLowerCase() + "%");
	    } else if (matchType.equalsIgnoreCase("contains")) {
		predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(field)),
			"%" + value.toLowerCase() + "%");
	    } else {
		throw new IllegalArgumentException("Invalid match type: " + matchType);
	    }
	    return predicate;
	};
    }

}
