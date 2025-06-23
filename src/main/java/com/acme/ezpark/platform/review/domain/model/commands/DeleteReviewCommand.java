package com.acme.ezpark.platform.review.domain.model.commands;


public record DeleteReviewCommand(
    Long reviewId,
    Long userId
) {
}
