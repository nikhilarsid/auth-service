package com.example.demo.auth.controller;

import com.example.demo.auth.dto.request.MerchantRegisterRequest;
import com.example.demo.auth.dto.request.UserRegisterRequest;
import com.example.demo.auth.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/customer")
    public ResponseEntity<String> registerCustomer(@RequestBody UserRegisterRequest request) {
        registrationService.registerCustomer(request);
        return ResponseEntity.ok("Customer registered successfully");
    }

    @PostMapping("/merchant")
    public ResponseEntity<String> registerMerchant(@RequestBody MerchantRegisterRequest request) {
        registrationService.registerMerchant(request);
        return ResponseEntity.ok("Merchant registered successfully");
    }
}