package com.stock.inventorymanagement.enums;

public enum Operator {

    EQUALS("equals"), NOT_EQUALS("notEquals"), GREATER_THAN("greaterThan"), LESS_THAN("lessThan"),
    GREATER_THAN_OR_EQUALS("greaterThanOrEquals"), LESS_THAN_OR_EQUALS("lessThanOrEquals"), CONTAINS("contains"),
    STARTS_WITH("startsWith"), ENDS_WITH("endsWith");

    private final String value;

    Operator(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    

    public static Operator fromValue(String value) {
        for (Operator operator : Operator.values()) {
            if (operator.value.equalsIgnoreCase(value)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unsupported operator: " + value);
    }

}
