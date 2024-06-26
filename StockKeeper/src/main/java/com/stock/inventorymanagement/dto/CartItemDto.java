package com.stock.inventorymanagement.dto;

import java.math.BigDecimal;

public class CartItemDto {

    private Long id;
    private Long productId;
    private int quantity;
    private BigDecimal price;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getProductId() {
	return productId;
    }

    public void setProductId(Long productId) {
	this.productId = productId;
    }

    public int getQuantity() {
	return quantity;
    }

    public void setQuantity(int quantity) {
	this.quantity = quantity;
    }

    public BigDecimal getPrice() {
	return price;
    }

    public void setPrice(BigDecimal price) {
	this.price = price;
    }

}
