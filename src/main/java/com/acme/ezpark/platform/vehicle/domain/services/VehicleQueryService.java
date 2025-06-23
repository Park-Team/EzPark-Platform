package com.acme.ezpark.platform.vehicle.domain.services;

import com.acme.ezpark.platform.vehicle.domain.model.aggregates.Vehicle;
import com.acme.ezpark.platform.vehicle.domain.model.queries.GetVehicleByIdQuery;
import com.acme.ezpark.platform.vehicle.domain.model.queries.GetVehiclesByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface VehicleQueryService {
    Optional<Vehicle> handle(GetVehicleByIdQuery query);
    List<Vehicle> handle(GetVehiclesByUserIdQuery query);
}
