package com.stock.inventorymanagement.enums;

public enum PriceType {

    PURCHASE_PRICE("Purchase Price"), SALES_PRICE("Sales Price"), WHOLESALE_PRICE("Wholesale Price"),
    DISCOUNT_PRICE("Discount Price"),BOX_PRICE("Box Price"),SUGGESTED_SELLING_PRICE("Suggested Selling Price");
    

    private final String type;

    PriceType(String type) {
	this.type = type;
    }

    public String getType() {
	return type;
    }

    public static boolean isValid(String priceType) {
	for (PriceType type : values()) {
	    if (type.type.equalsIgnoreCase(priceType)) {
		return true;
	    }
	}
	return false;
    }
}
