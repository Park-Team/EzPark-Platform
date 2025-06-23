package com.acme.ezpark.platform.review.interfaces.rest.resources;

import com.acme.ezpark.platform.review.domain.model.valueobjects.Rating;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;


@Schema(description = "Review Resource")
public record ReviewResource(
        @Schema(description = "Review ID", example = "1")
        Long id,
        
        @Schema(description = "User ID who wrote the review", example = "1")
        Long userId,
        
        @Schema(description = "Parking ID being reviewed", example = "1")
        Long parkingId,
        
        @Schema(description = "Booking ID that allows this review", example = "1")
        Long bookingId,
        
        @Schema(description = "Rating given to the parking", example = "FIVE")
        Rating rating,
        
        @Schema(description = "Comment about the parking", example = "Great parking space!")        String comment,
        
        @Schema(description = "Whether the review is active", example = "true")
        Boolean isActive,
        
        @Schema(description = "When the review was created", example = "2025-06-13T10:30:00.000Z")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        Date createdAt,
        
        @Schema(description = "When the review was last updated", example = "2025-06-13T10:30:00.000Z")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        Date updatedAt
) {
}
