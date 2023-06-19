package com.stock.inventorymanagement.dto;

import com.stock.inventorymanagement.enums.Operator;

public class FieldCriterion<T> {

    private String field;
    private String operator;
    private T value;

    public FieldCriterion(String field, String operator, T value) {
	this.field = field;
	this.operator = operator;
	this.value = value;
    }

    public String getField() {
	return field;
    }

    public void setField(String field) {
	this.field = field;
    }

    public Operator getOperator() {
	return Operator.fromValue(operator);
    }

    public void setOperator(String operator) {
	this.operator = operator;
    }

    public T getValue() {
	return value;
    }

    public void setValue(T value) {
	this.value = value;
    }
}
