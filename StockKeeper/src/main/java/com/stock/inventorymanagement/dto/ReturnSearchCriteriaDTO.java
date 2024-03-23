package com.stock.inventorymanagement.dto;

public class ReturnSearchCriteriaDTO {

    private String field;
    private String value;
    private String operator;


    public ReturnSearchCriteriaDTO(String field, String value, String operator) {
        this.field = field;
        this.value = value;
        this.operator = operator;
    }

    // Getters and setters
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
