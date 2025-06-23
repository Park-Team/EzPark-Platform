package com.acme.ezpark.platform.review.interfaces.rest.transform;

import com.acme.ezpark.platform.review.domain.model.aggregates.Review;
import com.acme.ezpark.platform.review.interfaces.rest.resources.ReviewResource;


public class ReviewResourceFromEntityAssembler {
    

    public static ReviewResource toResourceFromEntity(Review entity) {
        return new ReviewResource(
                entity.getId(),
                entity.getUserId(),
                entity.getParkingId(),
                entity.getBookingId(),
                entity.getRating(),
                entity.getComment(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
