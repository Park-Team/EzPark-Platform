package com.acme.ezpark.platform.parking.domain.services;

import com.acme.ezpark.platform.parking.domain.model.aggregates.Parking;
import com.acme.ezpark.platform.parking.domain.model.commands.CreateParkingCommand;
import com.acme.ezpark.platform.parking.domain.model.commands.DeleteParkingCommand;
import com.acme.ezpark.platform.parking.domain.model.commands.UpdateParkingCommand;

import java.util.Optional;

public interface ParkingCommandService {
    Optional<Parking> handle(CreateParkingCommand command);
    Optional<Parking> handle(UpdateParkingCommand command);
    boolean handle(DeleteParkingCommand command);
    boolean deleteParking(Long parkingId);
}
