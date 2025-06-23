package com.acme.ezpark.platform.vehicle.infrastructure.persistence.jpa.repositories;

import com.acme.ezpark.platform.vehicle.domain.model.aggregates.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByUserIdAndIsActiveTrue(Long userId);
    List<Vehicle> findByUserId(Long userId);
    boolean existsByLicensePlate(String licensePlate);
}
