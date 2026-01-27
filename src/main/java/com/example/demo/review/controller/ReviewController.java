package com.example.demo.review.controller;

import com.example.demo.review.dto.request.CreateReviewRequest;
import com.example.demo.review.dto.response.ReviewResponse;
import com.example.demo.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(@RequestBody CreateReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @GetMapping("/product/{merchantProductId}")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable Long merchantProductId) {
        return ResponseEntity.ok(reviewService.getReviewsForProduct(merchantProductId));
    }

    @GetMapping("/product/{merchantProductId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long merchantProductId) {
        return ResponseEntity.ok(reviewService.getAverageRating(merchantProductId));
    }
}