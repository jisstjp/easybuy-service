package com.stock.inventorymanagement.enums;

public enum OrderStatus {

    PENDING("Pending"), VERIFIED("Verified"), PAID("Paid"), PROCESSING("Processing"), SHIPPED("Shipped"),
    DELIVERED("Delivered"), CANCELLED("Cancelled"), RETURNED("Returned");

    private final String status;

    OrderStatus(String status) {
	this.status = status;
    }

    public String getStatus() {
	return status;
    } 

}
