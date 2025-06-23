package com.acme.ezpark.platform.vehicle.interfaces.rest.transform;

import com.acme.ezpark.platform.vehicle.domain.model.commands.CreateVehicleCommand;
import com.acme.ezpark.platform.vehicle.interfaces.rest.resources.CreateVehicleResource;

public class CreateVehicleCommandFromResourceAssembler {
    public static CreateVehicleCommand toCommandFromResource(Long userId, CreateVehicleResource resource) {
        return new CreateVehicleCommand(
            userId,
            resource.licensePlate(),
            resource.brand(),
            resource.model(),
            resource.color(),
            resource.year(),
            resource.vehicleType()
        );
    }
}
