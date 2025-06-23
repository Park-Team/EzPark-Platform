package com.acme.ezpark.platform.parking.interfaces.rest.transform;

import com.acme.ezpark.platform.parking.domain.model.aggregates.Parking;
import com.acme.ezpark.platform.parking.interfaces.rest.resources.ParkingResource;

public class ParkingResourceFromEntityAssembler {
    public static ParkingResource toResourceFromEntity(Parking entity) {
        return new ParkingResource(
            entity.getId(),
            entity.getOwnerId(),
            entity.getAddress(),
            entity.getLatitude(),
            entity.getLongitude(),
            entity.getWidth(),
            entity.getLength(),
            entity.getHeight(),
            entity.getPricePerHour(),
            entity.getDescription(),
            entity.getImageUrl(),
            entity.getIsAvailable(),
            entity.getParkingType()
        );
    }
}
