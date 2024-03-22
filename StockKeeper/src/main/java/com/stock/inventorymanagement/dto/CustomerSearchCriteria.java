package com.stock.inventorymanagement.dto;

import com.stock.inventorymanagement.enums.Operator;

public class CustomerSearchCriteria {

    private String field;
    private String value;
    private Operator operator;

    public String getField() {
	return field;
    }

    public void setField(String field) {
	this.field = field;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
}
