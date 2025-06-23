package com.acme.ezpark.platform.review.infrastructure.persistence.jpa.repositories;

import com.acme.ezpark.platform.review.domain.model.aggregates.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    

    Optional<Review> findByIdAndIsActiveTrue(Long id);
    

    List<Review> findByParkingIdAndIsActiveTrueOrderByCreatedAtDesc(Long parkingId);
    

    List<Review> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId);
    

    Optional<Review> findByBookingIdAndIsActiveTrue(Long bookingId);
    

    boolean existsByBookingIdAndIsActiveTrue(Long bookingId);
    

    @Query("SELECT AVG(CASE r.rating " +
           "WHEN 'ONE' THEN 1 " +
           "WHEN 'TWO' THEN 2 " +
           "WHEN 'THREE' THEN 3 " +
           "WHEN 'FOUR' THEN 4 " +
           "WHEN 'FIVE' THEN 5 " +
           "ELSE 0 END) " +
           "FROM Review r WHERE r.parkingId = :parkingId AND r.isActive = true")
    Double findAverageRatingByParkingId(@Param("parkingId") Long parkingId);
    

    Long countByParkingIdAndIsActiveTrue(Long parkingId);
    

    List<Review> findByIsActiveTrueOrderByCreatedAtDesc();
}
