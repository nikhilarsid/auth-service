package com.example.demo.auth.dto.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
}