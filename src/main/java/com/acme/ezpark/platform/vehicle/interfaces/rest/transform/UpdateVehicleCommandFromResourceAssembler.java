package com.acme.ezpark.platform.vehicle.interfaces.rest.transform;

import com.acme.ezpark.platform.vehicle.domain.model.commands.UpdateVehicleCommand;
import com.acme.ezpark.platform.vehicle.interfaces.rest.resources.UpdateVehicleResource;

public class UpdateVehicleCommandFromResourceAssembler {
    public static UpdateVehicleCommand toCommandFromResource(Long vehicleId, UpdateVehicleResource resource) {
        return new UpdateVehicleCommand(
            vehicleId,
            resource.brand(),
            resource.model(),
            resource.color(),
            resource.year(),
            resource.vehicleType()
        );
    }
}
