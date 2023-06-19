package com.stock.inventorymanagement.service;

import java.util.List;

import com.stock.inventorymanagement.dto.CartDto;

public interface CartService {

    CartDto createCart(Long userId);

    CartDto getCartById(Long cartId);

    List<CartDto> getCartsByUserId(Long userId);

    void deleteCart(Long cartId);

}
