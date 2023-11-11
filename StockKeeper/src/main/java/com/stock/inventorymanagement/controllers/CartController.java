package com.stock.inventorymanagement.controllers;

import java.util.List;
import java.util.Map;

import com.stock.inventorymanagement.service.impl.PdfGenerationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stock.inventorymanagement.dto.CartDto;
import com.stock.inventorymanagement.dto.CartItemDto;
import com.stock.inventorymanagement.service.CartItemService;
import com.stock.inventorymanagement.service.CartService;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;


    @Autowired
    private PdfGenerationServiceImpl pdfGenerationService;

    @PostMapping("/create")
    public ResponseEntity<CartDto> createCart(@RequestBody Map<String, String> requestBody) {
	Long userId = Long.parseLong(requestBody.get("userId"));
	CartDto createdCart = cartService.createCart(userId);
	return ResponseEntity.status(HttpStatus.CREATED).body(createdCart);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCartById(@PathVariable Long cartId) {
	CartDto cart = cartService.getCartById(cartId);
	return ResponseEntity.ok(cart);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartDto>> getCartsByUserId(@PathVariable Long userId) {
	List<CartDto> carts = cartService.getCartsByUserId(userId);
	return ResponseEntity.ok(carts);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
	cartService.deleteCart(cartId);
	return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}/cart-items")
    public ResponseEntity<List<CartItemDto>> getCartItemsByCartId(@PathVariable Long cartId) {
	List<CartItemDto> cartItems = cartItemService.getCartItemsByCartId(cartId);
	return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/{cartId}/cart-items")
    public ResponseEntity<CartItemDto> addCartItem(@PathVariable Long cartId, @RequestBody CartItemDto cartItemDto) {
	CartItemDto createdCartItem = cartItemService.addCartItem(cartId, cartItemDto);
	return ResponseEntity.status(HttpStatus.CREATED).body(createdCartItem);
    }

    @PutMapping("/{cartId}/cart-items/{itemId}")
    public ResponseEntity<CartItemDto> updateCartItem(@PathVariable Long cartId, @PathVariable Long itemId,
	    @RequestBody CartItemDto cartItemDto) {
	CartItemDto updatedCartItem = cartItemService.updateCartItem(cartId, itemId, cartItemDto);
	return ResponseEntity.ok(updatedCartItem);
    }

    @DeleteMapping("/{cartId}/cart-items/{itemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartId, @PathVariable Long itemId) {
	cartItemService.deleteCartItem(cartId, itemId);
	return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/cart-items")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
	cartItemService.clearCart(cartId);
	return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cartId}/cart-items/multiple")
    public ResponseEntity<List<CartItemDto>> addMultipleCartItems(@PathVariable Long cartId,
	    @RequestBody List<CartItemDto> cartItems) {
	List<CartItemDto> createdCartItems = cartItemService.addCartItems(cartId, cartItems);
	return ResponseEntity.status(HttpStatus.CREATED).body(createdCartItems);
    }

    @PutMapping("/{cartId}/cart-items/multiple")
    public ResponseEntity<List<CartItemDto>> updateCartItems(@PathVariable Long cartId,
	    @RequestBody List<CartItemDto> cartItems) {
	List<CartItemDto> updatedCartItems = cartItemService.updateCartItems(cartId, cartItems);
	return ResponseEntity.ok(updatedCartItems);
    }

    @GetMapping(value = "/{cartId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateOrderSummaryPdf(@PathVariable Long cartId) {
        try {
            byte[] pdfContent = pdfGenerationService.generateOrderSummaryPreviewPdf(cartId);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=order_summary_" + cartId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent);
        } catch (Exception e) {
            // Exception handling can be more specific based on your application's needs
            return ResponseEntity.internalServerError().build();
        }
    }

}
