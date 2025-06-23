package com.acme.ezpark.platform.vehicle.domain.model.commands;

import com.acme.ezpark.platform.vehicle.domain.model.valueobjects.VehicleType;

public record CreateVehicleCommand(
    Long userId,
    String licensePlate,
    String brand,
    String model,
    String color,
    String year,
    VehicleType vehicleType
) {
}
