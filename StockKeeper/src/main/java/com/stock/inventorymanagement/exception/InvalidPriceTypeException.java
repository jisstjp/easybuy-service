package com.stock.inventorymanagement.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPriceTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidPriceTypeException(String message) {
		super(message);
	}

}
