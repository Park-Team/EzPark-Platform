package com.acme.ezpark.platform.review.interfaces.rest.transform;

import com.acme.ezpark.platform.review.domain.model.commands.DeleteReviewCommand;
import com.acme.ezpark.platform.review.interfaces.rest.resources.DeleteReviewResource;


public class DeleteReviewCommandFromResourceAssembler {
    

    public static DeleteReviewCommand toCommandFromResource(Long reviewId, DeleteReviewResource resource) {
        return new DeleteReviewCommand(
                reviewId,
                resource.userId()
        );
    }
}
