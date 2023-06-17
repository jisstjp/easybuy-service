package com.stock.inventorymanagement.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDto {

    private Long id;
    private Long userId;
    private Long cartId;
    private BigDecimal totalPrice;
    private PaymentDto payment;
    private String orderStatus;
    private List<OrderItemDto> orderItems;

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

    public BigDecimal getTotalPrice() {
	return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
	this.totalPrice = totalPrice;
    }

    public Long getCartId() {
	return cartId;
    }

    public void setCartId(Long cartId) {
	this.cartId = cartId;
    }

    public PaymentDto getPayment() {
	return payment;
    }

    public void setPayment(PaymentDto payment) {
	this.payment = payment;
    }

    public String getOrderStatus() {
	return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
	this.orderStatus = orderStatus;
    }

    public List<OrderItemDto> getOrderItems() {
	return orderItems;
    }

    public void setOrderItems(List<OrderItemDto> orderItems) {
	this.orderItems = orderItems;
    }

}
