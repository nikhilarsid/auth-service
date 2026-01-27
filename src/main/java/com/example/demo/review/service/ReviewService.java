package com.example.demo.review.service;

import com.example.demo.review.dto.request.CreateReviewRequest;
import com.example.demo.review.dto.response.ReviewResponse;
import com.example.demo.review.entity.Review;
import com.example.demo.review.repository.ReviewRepository;
import com.example.demo.auth.entity.User; // Importing User only for authentication context type
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewResponse createReview(CreateReviewRequest request) {
        // Get the currently logged-in user from the Security Context
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = Review.builder()
                .userId(currentUser.getId())
                .merchantProductId(request.getMerchantProductId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);
        return mapToResponse(savedReview);
    }

    public List<ReviewResponse> getReviewsForProduct(Long merchantProductId) {
        List<Review> reviews = reviewRepository.findByMerchantProductId(merchantProductId);
        return reviews.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Helper method to calculate average rating (Useful for Ranking Service)
    public Double getAverageRating(Long merchantProductId) {
        List<Review> reviews = reviewRepository.findByMerchantProductId(merchantProductId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        double sum = reviews.stream().mapToInt(Review::getRating).sum();
        return sum / reviews.size();
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .merchantProductId(review.getMerchantProductId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}