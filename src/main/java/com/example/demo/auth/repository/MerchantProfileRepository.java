package com.example.demo.auth.repository;

import com.example.demo.auth.entity.MerchantProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantProfileRepository extends JpaRepository<MerchantProfile, Long> {
}