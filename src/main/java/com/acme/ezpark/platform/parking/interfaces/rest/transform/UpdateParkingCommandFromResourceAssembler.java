package com.acme.ezpark.platform.parking.interfaces.rest.transform;

import com.acme.ezpark.platform.parking.domain.model.commands.UpdateParkingCommand;
import com.acme.ezpark.platform.parking.interfaces.rest.resources.UpdateParkingResource;

public class UpdateParkingCommandFromResourceAssembler {
    public static UpdateParkingCommand toCommandFromResource(Long parkingId, UpdateParkingResource resource) {
        return new UpdateParkingCommand(
            parkingId,
            resource.address(),
            resource.width(),
            resource.length(),
            resource.height(),
            resource.pricePerHour(),
            resource.description(),
            resource.parkingType()
        );
    }
}
