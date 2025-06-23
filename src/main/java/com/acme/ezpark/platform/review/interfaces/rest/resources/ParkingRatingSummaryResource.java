package com.acme.ezpark.platform.review.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Parking Rating Summary Resource")
public record ParkingRatingSummaryResource(
        @Schema(description = "Parking ID", example = "1")
        Long parkingId,
        
        @Schema(description = "Average rating", example = "4.25")
        Double averageRating,
        
        @Schema(description = "Total number of reviews", example = "12")
        Long reviewCount
) {
}
