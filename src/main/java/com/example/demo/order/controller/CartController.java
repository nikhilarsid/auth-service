package com.example.demo.order.controller;

import com.example.demo.order.dto.request.AddToCartRequest;
import com.example.demo.order.dto.response.CartResponse;
import com.example.demo.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request) {
        cartService.addToCart(request);
        return ResponseEntity.ok("Item added to cart");
    }

    @GetMapping
    public ResponseEntity<CartResponse> viewCart() {
        return ResponseEntity.ok(cartService.getMyCart());
    }
}