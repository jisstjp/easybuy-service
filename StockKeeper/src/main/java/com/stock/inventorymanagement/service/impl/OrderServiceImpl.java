package com.stock.inventorymanagement.service.impl;

import com.stock.inventorymanagement.domain.*;
import com.stock.inventorymanagement.dto.OrderDto;
import com.stock.inventorymanagement.dto.OrderItemDto;
import com.stock.inventorymanagement.dto.OrderSearchCriteria;
import com.stock.inventorymanagement.dto.PaymentDto;
import com.stock.inventorymanagement.enums.PriceType;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.OrderItemMapper;
import com.stock.inventorymanagement.mapper.OrderMapper;
import com.stock.inventorymanagement.mapper.PaymentMapper;
import com.stock.inventorymanagement.repository.*;
import com.stock.inventorymanagement.service.OrderService;
import com.stock.inventorymanagement.service.PaymentService;
import com.stock.inventorymanagement.specification.OrderSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private OrderSpecifications orderSpecifications;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderDto createOrder(Long userId, OrderDto orderDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = cartRepository.findByIdAndUserId(orderDto.getCartId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", orderDto.getCartId()));

        BigDecimal totalPrice = calculateTotalSalesPriceForCart(cart);

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



    private BigDecimal calculateTotalPriceFromProduct(Cart cart) {
        return cart.getCartItems().stream()
                .map(cartItem -> {
                    // Fetch the current price of the product
                    BigDecimal currentPrice = productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItem.getProductId()))
                            .getPrice();
                    // Calculate price by multiplying with quantity
                    return currentPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up all the prices
    }


    private BigDecimal calculateTotalSalesPriceForCart(Cart cart) {
        return cart.getCartItems().stream()
                .map(cartItem -> {
                    // Fetch the product to access its prices
                    Product product = productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItem.getProductId()));

                    // Find the sales price from the list of prices associated with the product
                    Double salesPrice = product.getPrices().stream()
                            .filter(price -> price.getPriceType() == PriceType.SALES_PRICE && !price.isDeleted())
                            .map(Price::getPrice)
                            .findFirst()
                            .orElse(0.0);

                    // Calculate price by multiplying with the quantity
                    return BigDecimal.valueOf(salesPrice).multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up all the prices
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
        Payment payment = paymentRepository.findByOrderId(order.getId()).orElse(null);

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

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrdersByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);
        List<OrderDto> orderDtos = orderPage.getContent().stream().map(order -> {
            OrderDto orderDto = orderMapper.toDto(order);
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            List<OrderItemDto> orderItemDtos = orderItems.stream().map(orderItemMapper::toDto)
                    .collect(Collectors.toList());
            orderDto.setOrderItems(orderItemDtos);
            return orderDto;
        }).collect(Collectors.toList());
        return new PageImpl<>(orderDtos, pageable, orderPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> searchOrders(OrderSearchCriteria searchCriteria, Pageable pageable) {
        Specification<Order> specification = orderSpecifications.buildSpecification(searchCriteria.getField(),
                searchCriteria.getValue(), searchCriteria.getMatchType());
        Page<Order> orderPage = orderRepository.findAll(specification, pageable);
        List<OrderDto> orderDtoList = orderPage.getContent().stream().map(orderMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(orderDtoList, pageable, orderPage.getTotalElements());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderDto updateOrder(Long orderId, OrderDto orderDto) {
        // Retrieve the existing order from the database
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Update the relevant fields of the order using the values from the orderDto
        // existingOrder.setTotalPrice(orderDto.getTotalPrice());
        if (orderDto.getShippingAddress() != null) {
            existingOrder.setShippingAddress(orderDto.getShippingAddress());
        }
        if (orderDto.getOrderStatus() != null) {
            existingOrder.setOrderStatus(orderDto.getOrderStatus());
        }

        // Find the associated payment entity using the orderId
        Optional<Payment> optionalPayment = paymentRepository.findByOrderId(orderId);

        // Update the payment status if the payment entity exists and the paymentStatus
        // is provided
        optionalPayment.ifPresent(payment -> {
            if (orderDto.getPayment() != null && orderDto.getPayment().getStatus() != null) {
                payment.setStatus(orderDto.getPayment().getStatus());
                paymentRepository.save(payment);
            }
        });

        // Save the updated order
        Order updatedOrder = orderRepository.save(existingOrder);

        // Convert the updated order to OrderDto and return
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
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

}
