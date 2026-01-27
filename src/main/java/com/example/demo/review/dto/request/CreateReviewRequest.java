package com.example.demo.review.dto.request;

import lombok.Data;

@Data
public class CreateReviewRequest {
    private Long merchantProductId;
    private Integer rating;
    private String comment;
}