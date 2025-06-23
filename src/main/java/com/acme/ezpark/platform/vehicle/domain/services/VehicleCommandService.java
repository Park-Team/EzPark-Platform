package com.acme.ezpark.platform.vehicle.domain.services;

import com.acme.ezpark.platform.vehicle.domain.model.aggregates.Vehicle;
import com.acme.ezpark.platform.vehicle.domain.model.commands.CreateVehicleCommand;
import com.acme.ezpark.platform.vehicle.domain.model.commands.DeleteVehicleCommand;
import com.acme.ezpark.platform.vehicle.domain.model.commands.UpdateVehicleCommand;

import java.util.Optional;

public interface VehicleCommandService {
    Optional<Vehicle> handle(CreateVehicleCommand command);
    Optional<Vehicle> handle(UpdateVehicleCommand command);
    boolean handle(DeleteVehicleCommand command);
}
