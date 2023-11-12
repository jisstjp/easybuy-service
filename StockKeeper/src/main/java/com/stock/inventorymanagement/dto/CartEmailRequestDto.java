package com.stock.inventorymanagement.dto;

public class CartEmailRequestDto {
    private Long cartId;
    private String email;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
