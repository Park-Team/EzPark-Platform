package com.acme.ezpark.platform.vehicle.interfaces.rest.resources;

import com.acme.ezpark.platform.vehicle.domain.model.valueobjects.VehicleType;

public record VehicleResource(
    Long id,
    Long userId,
    String licensePlate,
    String brand,
    String model,
    String color,
    String year,
    VehicleType vehicleType,
    Boolean isActive
) {
}
