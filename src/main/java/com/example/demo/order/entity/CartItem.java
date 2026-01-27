package com.example.demo.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long merchantProductId; // Decoupled reference
    private Integer quantity;
    private Double price; // Store snapshot of price at time of adding

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}