package com.acme.ezpark.platform.review.domain.model.commands;

import com.acme.ezpark.platform.review.domain.model.valueobjects.Rating;


public record UpdateReviewCommand(
    Long reviewId,
    Long userId,
    Rating rating,
    String comment
) {
}
