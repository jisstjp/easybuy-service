package com.stock.inventorymanagement.exception;
public class InvalidPriceTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidPriceTypeException(String message) {
	super(message);
    }

}
