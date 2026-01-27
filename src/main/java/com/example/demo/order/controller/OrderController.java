package com.example.demo.order.controller;

import com.example.demo.order.dto.response.OrderResponse;
import com.example.demo.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout() {
        Long orderId = orderService.checkout();
        return ResponseEntity.ok("Order placed successfully! Order ID: " + orderId);
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderResponse>> getOrderHistory() {
        return ResponseEntity.ok(orderService.getOrderHistory());
    }
}