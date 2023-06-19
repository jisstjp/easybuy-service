package com.stock.inventorymanagement.dto;

public class OrderSearchCriteria {

    private String field;
    private String value;
    private String matchType;

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

    public String getMatchType() {
	return matchType;
    }

    public void setMatchType(String matchType) {
	this.matchType = matchType;
    }

}
