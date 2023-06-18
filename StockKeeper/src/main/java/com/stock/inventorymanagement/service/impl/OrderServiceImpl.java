package com.stock.inventorymanagement.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.inventorymanagement.domain.Cart;
import com.stock.inventorymanagement.domain.Order;
import com.stock.inventorymanagement.domain.OrderItem;
import com.stock.inventorymanagement.domain.Payment;
import com.stock.inventorymanagement.domain.User;
import com.stock.inventorymanagement.dto.OrderDto;
import com.stock.inventorymanagement.dto.OrderItemDto;
import com.stock.inventorymanagement.dto.PaymentDto;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.OrderItemMapper;
import com.stock.inventorymanagement.mapper.OrderMapper;
import com.stock.inventorymanagement.mapper.PaymentMapper;
import com.stock.inventorymanagement.repository.CartItemRepository;
import com.stock.inventorymanagement.repository.CartRepository;
import com.stock.inventorymanagement.repository.OrderItemRepository;
import com.stock.inventorymanagement.repository.OrderRepository;
import com.stock.inventorymanagement.repository.PaymentRepository;
import com.stock.inventorymanagement.repository.UserRepository;
import com.stock.inventorymanagement.service.OrderService;
import com.stock.inventorymanagement.service.PaymentService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderDto createOrder(Long userId, OrderDto orderDto) {
	User user = userRepository.findById(userId)
		.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

	Cart cart = cartRepository.findByIdAndUserId(orderDto.getCartId(), userId)
		.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", orderDto.getCartId()));

	BigDecimal totalPrice = calculateTotalPrice(cart);

	Order order = new Order();
	order.setUser(user);
	order.setTotalPrice(totalPrice);
	order.setOrderStatus(Optional.ofNullable(orderDto.getOrderStatus()).orElse("Pending"));
	String shippingAddress = orderDto.getShippingAddress();
	if (shippingAddress != null && shippingAddress.length() > 2000) {
	    shippingAddress = shippingAddress.substring(0, 2000);
	}
	order.setShippingAddress(shippingAddress);

	order.setCreatedAt(LocalDateTime.now());
	order.setUpdatedAt(LocalDateTime.now());

	Order savedOrder = orderRepository.save(order);

	List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
	    OrderItem orderItem = new OrderItem();
	    orderItem.setOrder(savedOrder);
	    orderItem.setProductId(cartItem.getProductId());
	    orderItem.setQuantity(cartItem.getQuantity());
	    orderItem.setPrice(cartItem.getPrice());
	    orderItem.setDeleted(false);
	    orderItem.setCreatedAt(LocalDateTime.now());
	    orderItem.setUpdatedAt(LocalDateTime.now());
	    return orderItem;
	}).collect(Collectors.toList());

	orderItemRepository.saveAll(orderItems);
	paymentService.processPayment(savedOrder, orderDto);
	cartItemRepository.markCartItemsAsDeleted(orderDto.getCartId());
	cartRepository.markCartAsDeleted(orderDto.getCartId());

	return mapToDto(savedOrder);
    }

    private BigDecimal calculateTotalPrice(Cart cart) {
	BigDecimal totalPrice = cart.getCartItems().stream()
		.map(cartItem -> cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
		.reduce(BigDecimal.ZERO, BigDecimal::add);

	return totalPrice;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(Long userId, Long orderId) {
	Order order = orderRepository.findByIdAndUserId(orderId, userId)
		.orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

	OrderDto orderDto = orderMapper.toDto(order);

	List<OrderItemDto> orderItemDtos = orderItemRepository.findByOrderId(orderId).stream()
		.map(orderItem -> orderItemMapper.toDto(orderItem)).collect(Collectors.toList());
	orderDto.setOrderItems(orderItemDtos);

	Payment payment = paymentRepository.findByOrderId(orderId)
		.orElseThrow(() -> new ResourceNotFoundException("Payment", "order_id", orderId));
	PaymentDto paymentDto = paymentMapper.toDto(payment);
	orderDto.setPayment(paymentDto);

	return orderDto;
    }
    
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Check if the order is already cancelled
        if (order.getOrderStatus().equals("Cancelled")) {
            throw new IllegalStateException("Order is already cancelled.");
        }

	// Update the order status to "Cancelled"
        order.setOrderStatus("Cancelled");


        // Save the updated order
        orderRepository.save(order);

        // Cancel the associated payment, if applicable
        Payment payment = paymentRepository.findByOrderId(order.getId())
                .orElse(null);

        if (payment != null) {
            payment.setStatus("Cancelled");
            paymentRepository.save(payment);
        }
    }

    private OrderDto mapToDto(Order order) {
	OrderDto orderDto = new OrderDto();
	orderDto.setId(order.getId());
	orderDto.setTotalPrice(order.getTotalPrice());
	return orderDto;
    }

}