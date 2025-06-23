package com.acme.ezpark.platform.review.interfaces.rest.transform;

import com.acme.ezpark.platform.review.domain.model.commands.CreateReviewCommand;
import com.acme.ezpark.platform.review.interfaces.rest.resources.CreateReviewResource;


public class CreateReviewCommandFromResourceAssembler {
    

    public static CreateReviewCommand toCommandFromResource(CreateReviewResource resource) {
        return new CreateReviewCommand(
                resource.userId(),
                resource.parkingId(), 
                resource.bookingId(),
                resource.rating(),
                resource.comment()
        );
    }
}
