package com.acme.ezpark.platform.parking.interfaces.rest.transform;

import com.acme.ezpark.platform.parking.domain.model.commands.CreateParkingCommand;
import com.acme.ezpark.platform.parking.interfaces.rest.resources.CreateParkingResource;

public class CreateParkingCommandFromResourceAssembler {
    public static CreateParkingCommand toCommandFromResource(Long ownerId, CreateParkingResource resource) {
        return new CreateParkingCommand(
            ownerId,
            resource.address(),
            resource.latitude(),
            resource.longitude(),
            resource.width(),
            resource.length(),
            resource.height(),
            resource.pricePerHour(),
            resource.description(),
            resource.parkingType(),
            resource.imageUrls()
        );
    }
}
