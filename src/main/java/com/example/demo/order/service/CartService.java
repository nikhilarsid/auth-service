package com.example.demo.order.service;

import com.example.demo.auth.entity.User;
import com.example.demo.order.dto.request.AddToCartRequest;
import com.example.demo.order.dto.response.CartResponse;
import com.example.demo.order.entity.Cart;
import com.example.demo.order.entity.CartItem;
import com.example.demo.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List; // <--- ADDED THIS IMPORT
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    @Transactional
    public void addToCart(AddToCartRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(user.getId())
                            .items(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });

        // Check if item already exists
        boolean exists = false;
        for (CartItem item : cart.getItems()) {
            if (item.getMerchantProductId().equals(request.getMerchantProductId())) {
                item.setQuantity(item.getQuantity() + request.getQuantity());
                exists = true;
                break;
            }
        }

        if (!exists) {
            CartItem newItem = CartItem.builder()
                    .merchantProductId(request.getMerchantProductId())
                    .quantity(request.getQuantity())
                    .price(request.getPrice())
                    .build();
            cart.addItem(newItem);
        }

        cartRepository.save(cart);
    }

    public CartResponse getMyCart() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        double totalValue = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        List<CartResponse.CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> CartResponse.CartItemDTO.builder()
                        .merchantProductId(item.getMerchantProductId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subTotal(item.getPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemDTOs)
                .totalValue(totalValue)
                .build();
    }

    @Transactional
    public void clearCart(Long userId) {
        cartRepository.findByUserId(userId).ifPresent(cartRepository::delete);
    }
}