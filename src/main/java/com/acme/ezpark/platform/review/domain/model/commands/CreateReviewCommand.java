package com.acme.ezpark.platform.review.domain.model.commands;

import com.acme.ezpark.platform.review.domain.model.valueobjects.Rating;


public record CreateReviewCommand(
    Long userId,
    Long parkingId,
    Long bookingId,
    Rating rating,
    String comment
) {
}
