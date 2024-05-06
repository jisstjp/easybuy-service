package com.stock.inventorymanagement.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import com.stock.inventorymanagement.enums.Operator;
import com.stock.inventorymanagement.util.CommonUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Customer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerSpecification {

	/*
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
    *
	 */

	public Specification<Customer> searchCustomers(String field, String value, Operator operator) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			Path<?> fieldPath = root.get(field); // Use Path<?>

			if (CommonUtils.isNumericOrCommaSeparatedNumeric(value)) {
				// Handle numeric searches
				return handleNumericSearch(fieldPath, value, operator, criteriaBuilder);
			} else {
				// Handle text-based searches
				return handleTextSearch(fieldPath, value, operator, criteriaBuilder);
			}
		};
	}




	private Predicate handleNumericSearch(Path<?> fieldPath, String value, Operator operator, CriteriaBuilder criteriaBuilder) {
		// Cast fieldPath to the appropriate numeric type (e.g., Double, Integer) based on your field's data type
		Path<Number> numericFieldPath = (Path<Number>) fieldPath;


		// Implement handling for numeric searches based on the provided operator
		switch (operator) {
			case EQUALS:
				return criteriaBuilder.equal(numericFieldPath, Double.parseDouble(value));
			case GREATER_THAN:
				return criteriaBuilder.gt(numericFieldPath, Double.parseDouble(value));
			case LESS_THAN:
				return criteriaBuilder.lt(numericFieldPath, Double.parseDouble(value));
			case IN_OPERATOR :
				List<Double> numbers = Arrays.stream(value.split(","))
						.map(String::trim)
						.map(Double::valueOf)
						.collect(Collectors.toList());;
				return fieldPath.in(numbers);

			// Add more cases as needed
			default:
				throw new IllegalArgumentException("Unsupported operator for numeric field: " + operator);
		}
	}


	private Predicate handleTextSearch(Path<?> fieldPath, String value, Operator operator, CriteriaBuilder criteriaBuilder) {
		// Implement handling for text-based searches based on the provided operator
		String searchValue = "%" + value.toLowerCase() + "%";

		switch (operator) {
			case EQUALS:
				return criteriaBuilder.equal(criteriaBuilder.lower((Expression<String>) fieldPath), value.toLowerCase());
			case CONTAINS:
				return criteriaBuilder.like(criteriaBuilder.lower((Expression<String>) fieldPath), searchValue);
			case STARTS_WITH:
				return criteriaBuilder.like(criteriaBuilder.lower((Expression<String>) fieldPath), value.toLowerCase() + "%");
			case ENDS_WITH:
				return criteriaBuilder.like(criteriaBuilder.lower((Expression<String>) fieldPath), "%" + value.toLowerCase());
			// Add more cases as needed
			default:
				throw new IllegalArgumentException("Unsupported operator for text field: " + operator);
		}
	}

}
