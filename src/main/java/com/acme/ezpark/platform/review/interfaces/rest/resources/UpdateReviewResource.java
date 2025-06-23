package com.acme.ezpark.platform.review.interfaces.rest.resources;

import com.acme.ezpark.platform.review.domain.model.valueobjects.Rating;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Update Review Resource")
public record UpdateReviewResource(
        @Schema(description = "User ID (must be the owner of the review)", example = "1", required = true)
        Long userId,
        
        @Schema(description = "New rating for the parking", example = "FOUR", required = true)
        Rating rating,
        
        @Schema(description = "Updated comment about the parking", example = "Good parking space, but a bit expensive.")
        String comment
) {
}
