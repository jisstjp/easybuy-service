package com.stock.inventorymanagement.service;

import java.util.List;

import com.stock.inventorymanagement.dto.CartItemDto;

public interface CartItemService {

    List<CartItemDto> getCartItemsByCartId(Long cartId);

    CartItemDto addCartItem(Long cartId, CartItemDto cartItemDto);
    
    List<CartItemDto> addCartItems(Long cartId, List<CartItemDto> cartItemDto);

    CartItemDto updateCartItem(Long cartId, Long itemId, CartItemDto cartItemDto);

    void deleteCartItem(Long cartId, Long itemId);
    
    void clearCart(Long cartId);
    
    List<CartItemDto> updateCartItems(Long cartId, List<CartItemDto> cartItems);
}
