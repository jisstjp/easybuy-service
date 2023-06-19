package com.stock.inventorymanagement.dto;

import java.util.List;

public class CartDto {
    private Long id;
    private Long userId;
   
    private List<CartItemDto> cartItems;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public List<CartItemDto> getCartItems() {
	return cartItems;
    }

    public void setCartItems(List<CartItemDto> cartItems) {
	this.cartItems = cartItems;
    }

}
