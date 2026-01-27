package com.example.demo.order.service;

import com.example.demo.auth.entity.User;
import com.example.demo.order.dto.response.OrderResponse;
import com.example.demo.order.entity.Cart;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderItem;
import com.example.demo.order.integration.OrderNotificationPublisher; // Import the publisher
import com.example.demo.order.repository.CartRepository;
import com.example.demo.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderNotificationPublisher orderNotificationPublisher; // Inject the publisher

    @Transactional
    public Long checkout() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart is empty, cannot checkout"));

        if(cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total
        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Create Order
        Order order = Order.builder()
                .userId(user.getId())
                .totalAmount(totalAmount)
                .status("CONFIRMED")
                .build();

        // Convert CartItems to OrderItems
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .merchantProductId(cartItem.getMerchantProductId())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getPrice())
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // 1. Empty the cart (Must happen BEFORE return)
        cartRepository.delete(cart);

        // 2. Send Email Notification
        orderNotificationPublisher.sendOrderConfirmation(savedOrder, user.getEmail());

        // 3. Return the ID
        return savedOrder.getId();
    }

    public List<OrderResponse> getOrderHistory() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(order -> OrderResponse.builder()
                        .orderId(order.getId())
                        .totalAmount(order.getTotalAmount())
                        .status(order.getStatus())
                        .orderDate(order.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}