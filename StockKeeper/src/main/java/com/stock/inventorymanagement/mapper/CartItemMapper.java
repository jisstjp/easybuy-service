package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.CartItem;
import com.stock.inventorymanagement.dto.CartItemDto;

@Component
public class CartItemMapper {
    @Autowired 
    private  ModelMapper modelMapper;
    
    public CartItemDto toDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemDto.class);
    }

    public CartItem toEntity(CartItemDto cartItemDto) {
        return modelMapper.map(cartItemDto, CartItem.class);
    }

}
