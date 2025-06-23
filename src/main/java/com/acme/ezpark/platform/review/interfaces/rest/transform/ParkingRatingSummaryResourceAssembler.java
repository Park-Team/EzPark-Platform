package com.acme.ezpark.platform.review.interfaces.rest.transform;

import com.acme.ezpark.platform.review.interfaces.rest.resources.ParkingRatingSummaryResource;


public class ParkingRatingSummaryResourceAssembler {
    

    public static ParkingRatingSummaryResource toResource(Long parkingId, Double averageRating, Long reviewCount) {
        return new ParkingRatingSummaryResource(
                parkingId,
                averageRating,
                reviewCount
        );
    }
}
