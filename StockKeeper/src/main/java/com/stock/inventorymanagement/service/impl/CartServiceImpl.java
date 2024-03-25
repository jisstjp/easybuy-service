package com.stock.inventorymanagement.service.impl;

import com.stock.inventorymanagement.domain.Cart;
import com.stock.inventorymanagement.domain.User;
import com.stock.inventorymanagement.dto.CartDto;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.CartMapper;
import com.stock.inventorymanagement.repository.CartRepository;
import com.stock.inventorymanagement.repository.UserRepository;
import com.stock.inventorymanagement.service.CartService;
import com.stock.inventorymanagement.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PdfGenerationServiceImpl pdfGenerationService;

    @Autowired
    private IEmailService emailService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CartDto createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if (cartRepository.existsByUserId(userId)) {
            throw new IllegalStateException("A cart already exists for this user.");
        }

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

    public void generateAndSendCartPdf(Long cartId, String recipientEmail, Long userId, boolean isAdminOrManager,boolean storeCreditAdd) throws MessagingException, MessagingException {
        try {
            // Generate PDF content for the cart
            byte[] pdfContent = pdfGenerationService.generateOrderSummaryPreviewPdf(cartId, userId, isAdminOrManager,storeCreditAdd);

            // Define the filename for the PDF attachment
            String attachmentFilename = "cart_summary_" + cartId + ".pdf";

            // Define the subject and body of the email
            String subject = "Cart Summary";
            String body = "Please find attached the summary of your cart.";

            // Send the email with the PDF attachment
            emailService.sendEmailWithAttachment(recipientEmail, subject, body, attachmentFilename, pdfContent);
        } catch (Exception e) {
            throw e;
            // Handle exceptions appropriately
        }
    }

}
