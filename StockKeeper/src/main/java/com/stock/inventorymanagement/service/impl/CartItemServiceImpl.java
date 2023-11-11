package com.stock.inventorymanagement.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.inventorymanagement.domain.Cart;
import com.stock.inventorymanagement.domain.CartItem;
import com.stock.inventorymanagement.domain.Product;
import com.stock.inventorymanagement.dto.CartItemDto;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.CartItemMapper;
import com.stock.inventorymanagement.repository.CartItemRepository;
import com.stock.inventorymanagement.repository.CartRepository;
import com.stock.inventorymanagement.repository.ProductRepository;
import com.stock.inventorymanagement.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl productService;

    @Override
    @Transactional(readOnly = true)
    public List<CartItemDto> getCartItemsByCartId(Long cartId) {

	List<CartItem> cartItems = cartItemRepository.findByCartIdWithoutDeleted(cartId);
	return cartItems.stream().map(cartItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CartItemDto addCartItem(Long cartId, CartItemDto cartItemDto) {

	Cart cart = cartRepository.findByIdAndIsDeletedFalse(cartId)
		.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));
	CartItem cartItem = cartItemMapper.toEntity(cartItemDto);

	// Check if the product exists and is not deleted
	Product product = productRepository.findByIdAndIsDeletedFalse(cartItemDto.getProductId())
		.orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItemDto.getProductId()));

	// Set the cartId on the cartItem entity
	cartItem.setProductId(product.getId());
    cartItem.setPrice(safelyFetchSalesPrice(product.getId(),cartItem.getPrice()));

	cartItem.setCart(cart);
	cartItem.setCreatedAt(LocalDateTime.now());
	cartItem.setUpdatedAt(LocalDateTime.now());
	cartItem.setDeleted(false);
	CartItem savedCartItem = cartItemRepository.save(cartItem);
	return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CartItemDto updateCartItem(Long cartId, Long itemId, CartItemDto cartItemDto) {
	// Check if the cart item exists in the cart and is not soft-deleted
	CartItem existingCartItem = cartItemRepository.findByCartIdAndItemIdWithoutDeleted(cartId, itemId)
		.orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));

        // Check if the product exists and is not deleted
        Product product = productRepository.findByIdAndIsDeletedFalse(cartItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItemDto.getProductId()));

	existingCartItem.setQuantity(cartItemDto.getQuantity());
    existingCartItem.setPrice(safelyFetchSalesPrice(product.getId(),existingCartItem.getPrice()));

        // Update other fields as needed
	existingCartItem.setUpdatedAt(LocalDateTime.now());
	CartItem savedCartItem = cartItemRepository.save(existingCartItem);
	return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCartItem(Long cartId, Long itemId) {
	// Check if the cart item exists in the cart and is not soft-deleted
	CartItem existingCartItem = cartItemRepository.findByCartIdAndItemIdWithoutDeleted(cartId, itemId)
		.orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));

	// Perform soft delete by updating the deleted flag and setting the updated
	// timestamp
	existingCartItem.setDeleted(true);
	existingCartItem.setUpdatedAt(LocalDateTime.now());
	cartItemRepository.save(existingCartItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void clearCart(Long cartId) {
	// Check if the cart exists and is not deleted
	Cart cart = cartRepository.findByIdAndIsDeletedFalse(cartId)
		.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

	List<CartItem> cartItems = cartItemRepository.findByCartIdWithoutDeleted(cartId);

	// Soft delete all the cart items
	cartItems.forEach(cartItem -> {
	    cartItem.setDeleted(true);
	    cartItem.setUpdatedAt(LocalDateTime.now());
	});

	// Update the cart's timestamp
	cart.setUpdatedAt(LocalDateTime.now());
	cartRepository.save(cart);

    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<CartItemDto> addCartItems(Long cartId, List<CartItemDto> cartItems) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        List<CartItem> newCartItems = new ArrayList<>();
        
        List<Long> productIds = cartItems.stream()
                .map(CartItemDto::getProductId)
                .collect(Collectors.toList());


        List<Product> products = productRepository.findByIdIn(productIds);

        for (CartItemDto cartItemDto : cartItems) {
            Long productId = cartItemDto.getProductId();

            Product product = products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
    	    cartItem.setProductId(product.getId());
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartItem.setCreatedAt(LocalDateTime.now());
            cartItem.setUpdatedAt(LocalDateTime.now());
            cartItem.setDeleted(false);

            newCartItems.add(cartItem);
        }

        List<CartItem> createdCartItems = cartItemRepository.saveAll(newCartItems);


        return createdCartItems.stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<CartItemDto> updateCartItems(Long cartId, List<CartItemDto> cartItems) {
        // Retrieve the existing cart from the repository
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        // Create a map of cart item IDs to their corresponding DTOs for efficient lookup
        Map<Long, CartItemDto> cartItemDtoMap = cartItems.stream()
                .collect(Collectors.toMap(CartItemDto::getId, Function.identity()));

        // Retrieve the existing cart items from the cart
        List<CartItem> existingCartItems = cart.getCartItems();

        // Update the existing cart items with the provided data
        for (CartItem existingCartItem : existingCartItems) {
            Long cartItemId = existingCartItem.getId();
            CartItemDto updatedCartItemDto = cartItemDtoMap.get(cartItemId);

            // If a matching cart item DTO is found, update the cart item
            if (updatedCartItemDto != null) {
                existingCartItem.setQuantity(updatedCartItemDto.getQuantity());
                existingCartItem.setUpdatedAt(LocalDateTime.now());
            }
        }

        // Save the updated cart items
        List<CartItem> updatedCartItems = cartItemRepository.saveAll(existingCartItems);

        // Map the updated cart items to DTOs and return
        return updatedCartItems.stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toList());
    }

    private BigDecimal safelyFetchSalesPrice(Long productId, BigDecimal fallbackPrice) {
        try {
            return productService.getSalesPrice(productId);
        } catch (Exception e) {
            // Log the exception if necessary
            return fallbackPrice;
        }
    }


}
