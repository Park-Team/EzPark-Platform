package com.acme.ezpark.platform.review.domain.services;

import com.acme.ezpark.platform.review.domain.model.aggregates.Review;
import com.acme.ezpark.platform.review.domain.model.commands.CreateReviewCommand;
import com.acme.ezpark.platform.review.domain.model.commands.UpdateReviewCommand;
import com.acme.ezpark.platform.review.domain.model.commands.DeleteReviewCommand;

import java.util.Optional;

public interface ReviewCommandService {
    Optional<Review> handle(CreateReviewCommand command);
    Optional<Review> handle(UpdateReviewCommand command);
    Optional<Review> handle(DeleteReviewCommand command);
}
