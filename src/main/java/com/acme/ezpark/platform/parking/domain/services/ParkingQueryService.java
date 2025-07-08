package com.acme.ezpark.platform.parking.domain.services;

import com.acme.ezpark.platform.parking.domain.model.aggregates.Parking;
import com.acme.ezpark.platform.parking.domain.model.queries.GetAllParkingsQuery;
import com.acme.ezpark.platform.parking.domain.model.queries.GetParkingByIdQuery;
import com.acme.ezpark.platform.parking.domain.model.queries.GetParkingsByLocationQuery;
import com.acme.ezpark.platform.parking.domain.model.queries.GetParkingsByOwnerIdQuery;

import java.util.List;
import java.util.Optional;

public interface ParkingQueryService {
    Optional<Parking> handle(GetParkingByIdQuery query);
    List<Parking> handle(GetParkingsByLocationQuery query);
    List<Parking> handle(GetParkingsByOwnerIdQuery query);
    List<Parking> handle(GetAllParkingsQuery query);
}
