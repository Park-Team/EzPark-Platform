package com.acme.ezpark.platform.vehicle.application.internal;

import com.acme.ezpark.platform.vehicle.domain.model.aggregates.Vehicle;
import com.acme.ezpark.platform.vehicle.domain.model.commands.CreateVehicleCommand;
import com.acme.ezpark.platform.vehicle.domain.model.commands.DeleteVehicleCommand;
import com.acme.ezpark.platform.vehicle.domain.model.commands.UpdateVehicleCommand;
import com.acme.ezpark.platform.vehicle.domain.services.VehicleCommandService;
import com.acme.ezpark.platform.vehicle.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Vehicle> handle(CreateVehicleCommand command) {
        if (vehicleRepository.existsByLicensePlate(command.licensePlate())) {
            return Optional.empty();
        }

        var vehicle = new Vehicle(
            command.userId(),
            command.licensePlate(),
            command.brand(),
            command.model(),
            command.color(),
            command.year(),
            command.vehicleType()
        );

        try {
            vehicleRepository.save(vehicle);
            return Optional.of(vehicle);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Vehicle> handle(UpdateVehicleCommand command) {
        return vehicleRepository.findById(command.vehicleId())
            .map(vehicle -> {
                vehicle.updateVehicle(
                    command.brand(),
                    command.model(),
                    command.color(),
                    command.year(),
                    command.vehicleType()
                );
                vehicleRepository.save(vehicle);
                return vehicle;
            });
    }

    @Override
    public boolean handle(DeleteVehicleCommand command) {
        return vehicleRepository.findById(command.vehicleId())
            .map(vehicle -> {
                vehicle.deactivateVehicle();
                vehicleRepository.save(vehicle);
                return true;
            })
            .orElse(false);
    }
}
