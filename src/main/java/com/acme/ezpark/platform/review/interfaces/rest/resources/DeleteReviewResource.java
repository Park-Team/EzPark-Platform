package com.acme.ezpark.platform.review.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Delete Review Resource")
public record DeleteReviewResource(
        @Schema(description = "User ID (must be the owner of the review)", example = "1", required = true)
        Long userId
) {
}
