package com.stock.inventorymanagement.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Cart;
import com.stock.inventorymanagement.domain.CartItem;
import com.stock.inventorymanagement.dto.CartDto;

@Component
public class CartMapper {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    public CartMapper(ModelMapper modelMapper) {
	this.modelMapper = modelMapper;
    }

    public CartDto toDto(Cart cart) {
	CartDto cartDto = new CartDto();
	cartDto.setId(cart.getId());
	cartDto.setUserId(cart.getUser().getId());
	List<CartItem> cartItems = cart.getCartItems();
	if (cartItems != null && !cartItems.isEmpty()) {
	    cartDto.setCartItems(cartItems.stream().map(cartItemMapper::toDto).collect(Collectors.toList()));
	}

	return cartDto;
    }

    public Cart toEntity(CartDto cartDto) {
	return modelMapper.map(cartDto, Cart.class);
    }

}
