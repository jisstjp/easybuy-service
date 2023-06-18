package com.stock.inventorymanagement.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.inventorymanagement.domain.Cart;
import com.stock.inventorymanagement.domain.User;
import com.stock.inventorymanagement.dto.CartDto;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.CartMapper;
import com.stock.inventorymanagement.repository.CartRepository;
import com.stock.inventorymanagement.repository.UserRepository;
import com.stock.inventorymanagement.service.CartService;
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CartDto createCart(Long userId) {
	User user = userRepository.findById(userId)
		.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
	Cart cart = new Cart();
	cart.setUser(user);
	cart.setCreatedAt(LocalDateTime.now());
	cart.setUpdatedAt(LocalDateTime.now());
	cart.setDeleted(false);
	Cart savedCart = cartRepository.save(cart);
	return cartMapper.toDto(savedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartDto getCartById(Long cartId) {
	Cart cart = cartRepository.findByIdAndIsDeletedFalse(cartId)
		.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

	return cartMapper.toDto(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartDto> getCartsByUserId(Long userId) {
	List<Cart> carts = cartRepository.findNonDeletedCartsByUserId(userId);
	return carts.stream().map(cartMapper::toDto).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findByIdAndIsDeletedFalse(cartId)
        	.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

      cart.setDeleted(true);
        cartRepository.save(cart);
    }

}