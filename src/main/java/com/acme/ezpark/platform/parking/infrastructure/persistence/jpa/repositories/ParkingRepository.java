package com.acme.ezpark.platform.parking.infrastructure.persistence.jpa.repositories;

import com.acme.ezpark.platform.parking.domain.model.aggregates.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {
    List<Parking> findByOwnerIdAndIsActiveTrue(Long ownerId);
    List<Parking> findByOwnerId(Long ownerId);
    List<Parking> findAllByIsActiveTrue();
    
    @Query("SELECT p FROM Parking p WHERE p.isActive = true AND p.isAvailable = true")
    List<Parking> findAllActiveAndAvailableParkings();
    

    @Query(value = """
        SELECT * FROM parkings p 
        WHERE p.is_active = true AND p.is_available = true
        AND p.latitude BETWEEN (:latitude - :radiusKm/111.0) AND (:latitude + :radiusKm/111.0)
        AND p.longitude BETWEEN (:longitude - :radiusKm/111.0) AND (:longitude + :radiusKm/111.0)
        """, nativeQuery = true)
    List<Parking> findParkingsWithinRadius(@Param("latitude") BigDecimal latitude, 
                                          @Param("longitude") BigDecimal longitude, 
                                          @Param("radiusKm") BigDecimal radiusKm);
}
