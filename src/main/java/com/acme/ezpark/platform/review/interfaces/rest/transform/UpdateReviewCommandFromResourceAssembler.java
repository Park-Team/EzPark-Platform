package com.acme.ezpark.platform.review.interfaces.rest.transform;

import com.acme.ezpark.platform.review.domain.model.commands.UpdateReviewCommand;
import com.acme.ezpark.platform.review.interfaces.rest.resources.UpdateReviewResource;


public class UpdateReviewCommandFromResourceAssembler {
    

    public static UpdateReviewCommand toCommandFromResource(Long reviewId, UpdateReviewResource resource) {
        return new UpdateReviewCommand(
                reviewId,
                resource.userId(),
                resource.rating(),
                resource.comment()
        );
    }
}
