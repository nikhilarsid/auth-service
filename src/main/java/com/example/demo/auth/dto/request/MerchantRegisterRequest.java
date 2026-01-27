package com.example.demo.auth.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MerchantRegisterRequest extends UserRegisterRequest {
    private String businessName;
    private String businessAddress;
    private String gstNumber;
}