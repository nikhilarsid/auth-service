package com.example.demo.order.dto.request;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long merchantProductId;
    private Integer quantity;
    private Double price; // In real app, fetch this from Product Service to prevent tampering
}