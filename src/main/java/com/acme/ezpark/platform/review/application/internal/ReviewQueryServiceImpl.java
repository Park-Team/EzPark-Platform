package com.acme.ezpark.platform.review.application.internal;

import com.acme.ezpark.platform.review.domain.model.aggregates.Review;
import com.acme.ezpark.platform.review.domain.model.queries.GetReviewByIdQuery;
import com.acme.ezpark.platform.review.domain.model.queries.GetReviewsByParkingIdQuery;
import com.acme.ezpark.platform.review.domain.model.queries.GetReviewsByUserIdQuery;
import com.acme.ezpark.platform.review.domain.services.ReviewQueryService;
import com.acme.ezpark.platform.review.infrastructure.persistence.jpa.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ReviewQueryServiceImpl implements ReviewQueryService {
    
    private final ReviewRepository reviewRepository;
    
    @Autowired
    public ReviewQueryServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    
    @Override
    public Optional<Review> handle(GetReviewByIdQuery query) {
        return reviewRepository.findByIdAndIsActiveTrue(query.reviewId());
    }
    
    @Override
    public List<Review> handle(GetReviewsByParkingIdQuery query) {
        return reviewRepository.findByParkingIdAndIsActiveTrueOrderByCreatedAtDesc(query.parkingId());
    }
    
    @Override
    public List<Review> handle(GetReviewsByUserIdQuery query) {
        return reviewRepository.findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(query.userId());
    }
    
    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }
    
    @Override
    public Double getAverageRatingByParkingId(Long parkingId) {
        Double average = reviewRepository.findAverageRatingByParkingId(parkingId);
        return average != null ? Math.round(average * 100.0) / 100.0 : 0.0;
    }
    
    @Override
    public Long getReviewCountByParkingId(Long parkingId) {
        return reviewRepository.countByParkingIdAndIsActiveTrue(parkingId);
    }
}
