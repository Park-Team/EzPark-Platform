package com.acme.ezpark.platform.parking.application.internal;

import com.acme.ezpark.platform.parking.domain.model.aggregates.Parking;
import com.acme.ezpark.platform.parking.domain.model.commands.CreateParkingCommand;
import com.acme.ezpark.platform.parking.domain.model.commands.DeleteParkingCommand;
import com.acme.ezpark.platform.parking.domain.model.commands.UpdateParkingCommand;
import com.acme.ezpark.platform.parking.domain.services.ParkingCommandService;
import com.acme.ezpark.platform.parking.infrastructure.persistence.jpa.repositories.ParkingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParkingCommandServiceImpl implements ParkingCommandService {

    private final ParkingRepository parkingRepository;

    public ParkingCommandServiceImpl(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }    @Override
    public Optional<Parking> handle(CreateParkingCommand command) {
        var parking = new Parking(
            command.ownerId(),
            command.address(),
            command.latitude(),
            command.longitude(),
            command.width(),
            command.length(),
            command.height(),
            command.pricePerHour(),
            command.description(),
            command.parkingType()
        );

        try {
            parkingRepository.save(parking);
            return Optional.of(parking);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Parking> handle(UpdateParkingCommand command) {        return parkingRepository.findById(command.parkingId())
            .map(parking -> {
                parking.updateParking(
                    command.address(),
                    command.width(),
                    command.length(),
                    command.height(),
                    command.pricePerHour(),
                    command.description(),
                    command.parkingType()
                );
                parkingRepository.save(parking);
                return parking;
            });
    }

    @Override
    public boolean handle(DeleteParkingCommand command) {
        return deleteParking(command.parkingId());
    }    @Override
    public boolean deleteParking(Long parkingId) {
        return parkingRepository.findById(parkingId)
            .map(parking -> {
                parkingRepository.delete(parking);
                return true;
            })
            .orElse(false);
    }
}
