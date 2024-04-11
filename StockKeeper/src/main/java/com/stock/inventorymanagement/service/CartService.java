package com.stock.inventorymanagement.service;

import java.math.BigDecimal;
import java.util.List;

import com.stock.inventorymanagement.dto.CartDto;

import javax.mail.MessagingException;

public interface CartService {

    CartDto createCart(Long userId);

    CartDto getCartById(Long cartId);

    List<CartDto> getCartsByUserId(Long userId);

    void deleteCart(Long cartId);
    public void generateAndSendCartPdf(Long cartId, String recipientEmail,Long userId,boolean isAdminOrManager,boolean storeCreditAdd) throws MessagingException;

    public void subtractCredit(Long customerId, BigDecimal amount);

}
