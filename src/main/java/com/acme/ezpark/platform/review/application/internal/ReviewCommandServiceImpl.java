package com.acme.ezpark.platform.review.application.internal;

import com.acme.ezpark.platform.booking.domain.model.aggregates.Booking;
import com.acme.ezpark.platform.booking.domain.model.valueobjects.BookingStatus;
import com.acme.ezpark.platform.booking.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.acme.ezpark.platform.review.domain.model.aggregates.Review;
import com.acme.ezpark.platform.review.domain.model.commands.CreateReviewCommand;
import com.acme.ezpark.platform.review.domain.model.commands.DeleteReviewCommand;
import com.acme.ezpark.platform.review.domain.model.commands.UpdateReviewCommand;
import com.acme.ezpark.platform.review.domain.services.ReviewCommandService;
import com.acme.ezpark.platform.review.infrastructure.persistence.jpa.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class ReviewCommandServiceImpl implements ReviewCommandService {
    
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    
    @Autowired
    public ReviewCommandServiceImpl(ReviewRepository reviewRepository, BookingRepository bookingRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
    }
    
    @Override
    @Transactional
    public Optional<Review> handle(CreateReviewCommand command) {
        try {
            Optional<Booking> bookingOpt = bookingRepository.findById(command.bookingId());
            if (bookingOpt.isEmpty()) {
                throw new RuntimeException("Booking not found with ID: " + command.bookingId());
            }
            
            Booking booking = bookingOpt.get();


            if (!booking.getUserId().equals(command.userId())) {
                throw new RuntimeException("User is not authorized to review this booking");
            }
            if (!booking.getStatus().equals(BookingStatus.COMPLETED)) {
                throw new RuntimeException("Can only review completed bookings");
            }
            

            if (!booking.getParkingId().equals(command.parkingId())) {
                throw new RuntimeException("Parking ID does not match booking");
            }
            

            if (reviewRepository.existsByBookingIdAndIsActiveTrue(command.bookingId())) {
                throw new RuntimeException("Review already exists for this booking");
            }
            

            Review review = new Review(
                command.userId(),
                command.parkingId(),
                command.bookingId(),
                command.rating(),
                command.comment()
            );
            
            Review savedReview = reviewRepository.save(review);
            return Optional.of(savedReview);
            
        } catch (Exception e) {
            throw new RuntimeException("Error creating review: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional
    public Optional<Review> handle(UpdateReviewCommand command) {
        try {
            Optional<Review> reviewOpt = reviewRepository.findByIdAndIsActiveTrue(command.reviewId());
            if (reviewOpt.isEmpty()) {
                throw new RuntimeException("Review not found with ID: " + command.reviewId());
            }
            
            Review review = reviewOpt.get();

            if (!review.canBeUpdatedBy(command.userId())) {
                throw new RuntimeException("User is not authorized to update this review");
            }
            
            // Update review
            review.updateReview(command.rating(), command.comment());
            Review updatedReview = reviewRepository.save(review);
            return Optional.of(updatedReview);
            
        } catch (Exception e) {
            throw new RuntimeException("Error updating review: " + e.getMessage(), e);
        }
    }
      @Override
    @Transactional
    public Optional<Review> handle(DeleteReviewCommand command) {
        try {
            Optional<Review> reviewOpt = reviewRepository.findByIdAndIsActiveTrue(command.reviewId());
            if (reviewOpt.isEmpty()) {
                throw new RuntimeException("Review not found with ID: " + command.reviewId());
            }
            
            Review review = reviewOpt.get();

            if (!review.canBeUpdatedBy(command.userId())) {
                throw new RuntimeException("User is not authorized to delete this review");
            }

            review.deactivate();
            Review deactivatedReview = reviewRepository.save(review);
            return Optional.of(deactivatedReview);
            
        } catch (Exception e) {
            throw new RuntimeException("Error deleting review: " + e.getMessage(), e);
        }
    }
}
