package com.acme.ezpark.platform.vehicle.application.internal;

import com.acme.ezpark.platform.vehicle.domain.model.aggregates.Vehicle;
import com.acme.ezpark.platform.vehicle.domain.model.queries.GetVehicleByIdQuery;
import com.acme.ezpark.platform.vehicle.domain.model.queries.GetVehiclesByUserIdQuery;
import com.acme.ezpark.platform.vehicle.domain.services.VehicleQueryService;
import com.acme.ezpark.platform.vehicle.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.vehicleId());
    }

    @Override
    public List<Vehicle> handle(GetVehiclesByUserIdQuery query) {
        return vehicleRepository.findByUserIdAndIsActiveTrue(query.userId());
    }
}
