package com.example.demo.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_profile")
public class CustomerProfile {

    @Id
    @Column(name = "user_id")
    private Long id; // This will hold the same value as user.id

    private String address;
    private String phoneNumber;

    @OneToOne
    @MapsId // CRITICAL: Copies ID from 'user' to 'id'
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}