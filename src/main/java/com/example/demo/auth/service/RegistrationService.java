package com.example.demo.auth.service;

import com.example.demo.auth.dto.request.MerchantRegisterRequest;
import com.example.demo.auth.dto.request.UserRegisterRequest;
import com.example.demo.auth.entity.CustomerProfile;
import com.example.demo.auth.entity.MerchantProfile;
import com.example.demo.auth.entity.Role;
import com.example.demo.auth.entity.User;
import com.example.demo.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerCustomer(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already taken");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        CustomerProfile customerProfile = CustomerProfile.builder()
                .user(user)
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();

        user.setCustomerProfile(customerProfile);
        userRepository.save(user);
    }

    @Transactional
    public void registerMerchant(MerchantRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already taken");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MERCHANT)
                .build();

        MerchantProfile merchantProfile = MerchantProfile.builder()
                .user(user)
                .businessName(request.getBusinessName())
                .businessAddress(request.getBusinessAddress())
                .gstNumber(request.getGstNumber())
                .build();

        user.setMerchantProfile(merchantProfile);
        userRepository.save(user);
    }
}