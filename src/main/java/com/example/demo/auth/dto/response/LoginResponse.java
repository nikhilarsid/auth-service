package com.example.demo.auth.dto.response; // <--- This line is critical

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String role;
    private String fullName;
    private Long userId;
}