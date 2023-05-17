package com.stock.inventorymanagement.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.stock.inventorymanagement.exception.ErrorResponse;
import com.stock.inventorymanagement.exception.InvalidPriceTypeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
	// Create a custom error response
	ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", ex.getMessage());

	// Return the error response with an appropriate HTTP status
	return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidPriceTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPriceTypeException(InvalidPriceTypeException ex) {
	// Create a custom error response
	ErrorResponse errorResponse = new ErrorResponse("Invalid Price Type", "Invalid Price Type");

	// Return the error response with an appropriate HTTP status
	return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
