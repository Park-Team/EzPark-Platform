package com.acme.ezpark.platform.review.interfaces.rest;

import com.acme.ezpark.platform.review.domain.model.aggregates.Review;
import com.acme.ezpark.platform.review.domain.model.queries.GetReviewByIdQuery;
import com.acme.ezpark.platform.review.domain.model.queries.GetReviewsByParkingIdQuery;
import com.acme.ezpark.platform.review.domain.model.queries.GetReviewsByUserIdQuery;
import com.acme.ezpark.platform.review.domain.services.ReviewCommandService;
import com.acme.ezpark.platform.review.domain.services.ReviewQueryService;
import com.acme.ezpark.platform.review.interfaces.rest.resources.*;
import com.acme.ezpark.platform.review.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/api/v1/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reviews", description = "Review Management Endpoints")
public class ReviewsController {
    
    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;
    
    @Autowired
    public ReviewsController(ReviewCommandService reviewCommandService, ReviewQueryService reviewQueryService) {
        this.reviewCommandService = reviewCommandService;
        this.reviewQueryService = reviewQueryService;
    }
    

    @PostMapping
    @Operation(summary = "Create a new review", description = "Creates a new review for a parking after completing a booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "422", description = "Business rule validation failed", content = @Content)
    })
    public ResponseEntity<ReviewResource> createReview(@RequestBody CreateReviewResource createReviewResource) {
        try {
            var createReviewCommand = CreateReviewCommandFromResourceAssembler.toCommandFromResource(createReviewResource);
            Optional<Review> review = reviewCommandService.handle(createReviewCommand);
            
            if (review.isPresent()) {
                var reviewResource = ReviewResourceFromEntityAssembler.toResourceFromEntity(review.get());
                return new ResponseEntity<>(reviewResource, HttpStatus.CREATED);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
    

    @GetMapping
    @Operation(summary = "Get all reviews", description = "Retrieves all active reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResource.class)))
    })
    public ResponseEntity<List<ReviewResource>> getAllReviews() {
        var reviews = reviewQueryService.getAllReviews();
        var reviewResources = reviews.stream()
                .map(ReviewResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(reviewResources);
    }
    

    @GetMapping("/{reviewId}")
    @Operation(summary = "Get review by ID", description = "Retrieves a specific review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResource.class))),
            @ApiResponse(responseCode = "404", description = "Review not found", content = @Content)
    })
    public ResponseEntity<ReviewResource> getReviewById(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId) {
        var getReviewByIdQuery = new GetReviewByIdQuery(reviewId);
        Optional<Review> review = reviewQueryService.handle(getReviewByIdQuery);
        
        if (review.isPresent()) {
            var reviewResource = ReviewResourceFromEntityAssembler.toResourceFromEntity(review.get());
            return ResponseEntity.ok(reviewResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    @PutMapping("/{reviewId}")
    @Operation(summary = "Update a review", description = "Updates an existing review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Review not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Business rule validation failed", content = @Content)
    })
    public ResponseEntity<ReviewResource> updateReview(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId,
            @RequestBody UpdateReviewResource updateReviewResource) {
        try {
            var updateReviewCommand = UpdateReviewCommandFromResourceAssembler.toCommandFromResource(reviewId, updateReviewResource);
            Optional<Review> review = reviewCommandService.handle(updateReviewCommand);
            
            if (review.isPresent()) {
                var reviewResource = ReviewResourceFromEntityAssembler.toResourceFromEntity(review.get());
                return ResponseEntity.ok(reviewResource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete a review", description = "Soft deletes a review (marks as inactive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Review not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Business rule validation failed", content = @Content)
    })
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId,
            @RequestBody DeleteReviewResource deleteReviewResource) {
        try {
            var deleteReviewCommand = DeleteReviewCommandFromResourceAssembler.toCommandFromResource(reviewId, deleteReviewResource);
            Optional<Review> deletedReview = reviewCommandService.handle(deleteReviewCommand);
            if (deletedReview.isPresent()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
    

    @GetMapping("/parking/{parkingId}")
    @Operation(summary = "Get reviews by parking ID", description = "Retrieves all reviews for a specific parking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResource.class)))
    })
    public ResponseEntity<List<ReviewResource>> getReviewsByParkingId(
            @Parameter(description = "Parking ID", required = true) @PathVariable Long parkingId) {
        var getReviewsByParkingIdQuery = new GetReviewsByParkingIdQuery(parkingId);
        var reviews = reviewQueryService.handle(getReviewsByParkingIdQuery);
        var reviewResources = reviews.stream()
                .map(ReviewResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(reviewResources);
    }
    

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get reviews by user ID", description = "Retrieves all reviews written by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResource.class)))
    })
    public ResponseEntity<List<ReviewResource>> getReviewsByUserId(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        var getReviewsByUserIdQuery = new GetReviewsByUserIdQuery(userId);
        var reviews = reviewQueryService.handle(getReviewsByUserIdQuery);
        var reviewResources = reviews.stream()
                .map(ReviewResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(reviewResources);
    }
    

    @GetMapping("/parking/{parkingId}/summary")
    @Operation(summary = "Get parking rating summary", description = "Retrieves rating statistics for a specific parking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating summary retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingRatingSummaryResource.class)))
    })
    public ResponseEntity<ParkingRatingSummaryResource> getParkingRatingSummary(
            @Parameter(description = "Parking ID", required = true) @PathVariable Long parkingId) {
        Double averageRating = reviewQueryService.getAverageRatingByParkingId(parkingId);
        Long reviewCount = reviewQueryService.getReviewCountByParkingId(parkingId);
        
        var summaryResource = ParkingRatingSummaryResourceAssembler.toResource(
                parkingId, averageRating, reviewCount);
        return ResponseEntity.ok(summaryResource);
    }
}
