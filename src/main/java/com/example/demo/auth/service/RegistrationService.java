package com.example.demo.auth.service;

import com.example.demo.auth.dto.response.LoginResponse; // Re-using LoginResponse is fine
import com.example.demo.auth.dto.request.MerchantRegisterRequest;
import com.example.demo.auth.dto.request.UserRegisterRequest;
import com.example.demo.auth.entity.CustomerProfile;
import com.example.demo.auth.entity.MerchantProfile;
import com.example.demo.auth.entity.Role;
import com.example.demo.auth.entity.User;
import com.example.demo.auth.repository.UserRepository;
import com.example.demo.auth.service.JwtService; // Need this injected
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // <--- Inject this

    @Transactional
    public LoginResponse registerCustomer(UserRegisterRequest request) {
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
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();

        // Bi-directional link
        customerProfile.setUser(user);
        user.setCustomerProfile(customerProfile);

        User savedUser = userRepository.save(user);

        // --- Generate Token ---
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", savedUser.getRole().name());
        extraClaims.put("userId", savedUser.getId());

        String jwtToken = jwtService.generateToken(extraClaims, savedUser);

        return LoginResponse.builder()
                .token(jwtToken)
                .role(savedUser.getRole().name())
                .fullName(savedUser.getFullName())
                .userId(savedUser.getId())
                .build();
    }

    @Transactional
    public LoginResponse registerMerchant(MerchantRegisterRequest request) {
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
                .businessName(request.getBusinessName())
                .businessAddress(request.getBusinessAddress())
                .gstNumber(request.getGstNumber())
                .build();

        // Bi-directional link
        merchantProfile.setUser(user);
        user.setMerchantProfile(merchantProfile);

        User savedUser = userRepository.save(user);

        // --- Generate Token ---
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", savedUser.getRole().name());
        extraClaims.put("userId", savedUser.getId());

        String jwtToken = jwtService.generateToken(extraClaims, savedUser);

        return LoginResponse.builder()
                .token(jwtToken)
                .role(savedUser.getRole().name())
                .fullName(savedUser.getFullName())
                .userId(savedUser.getId())
                .build();
    }
}