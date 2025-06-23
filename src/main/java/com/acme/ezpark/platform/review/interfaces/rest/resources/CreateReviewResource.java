package com.acme.ezpark.platform.review.interfaces.rest.resources;

import com.acme.ezpark.platform.review.domain.model.valueobjects.Rating;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Create Review Resource")
public record CreateReviewResource(
        @Schema(description = "User ID (Guest who writes the review)", example = "1", required = true)
        Long userId,
        
        @Schema(description = "Parking ID being reviewed", example = "1", required = true)
        Long parkingId,
        
        @Schema(description = "Booking ID that allows this review", example = "1", required = true)
        Long bookingId,
        
        @Schema(description = "Rating given to the parking", example = "FIVE", required = true)
        Rating rating,
        
        @Schema(description = "Optional comment about the parking", example = "Great parking space, very convenient location!")
        String comment
) {
}
