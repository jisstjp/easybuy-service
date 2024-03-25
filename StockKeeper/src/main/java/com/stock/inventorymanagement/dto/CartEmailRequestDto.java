package com.stock.inventorymanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


public class CartEmailRequestDto {
    private Long cartId;
    private String email;

    private boolean storeCreditAdd;

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

    public boolean isStoreCreditAdd() {
        return storeCreditAdd;
    }

    public void setStoreCreditAdd(boolean storeCreditAdd) {
        this.storeCreditAdd = storeCreditAdd;
    }
}
