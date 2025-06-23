package com.acme.ezpark.platform.vehicle.interfaces.rest.transform;

import com.acme.ezpark.platform.vehicle.domain.model.aggregates.Vehicle;
import com.acme.ezpark.platform.vehicle.interfaces.rest.resources.VehicleResource;

public class VehicleResourceFromEntityAssembler {
    public static VehicleResource toResourceFromEntity(Vehicle entity) {
        return new VehicleResource(
            entity.getId(),
            entity.getUserId(),
            entity.getLicensePlate(),
            entity.getBrand(),
            entity.getModel(),
            entity.getColor(),
            entity.getYear(),
            entity.getVehicleType(),
            entity.getIsActive()
        );
    }
}
