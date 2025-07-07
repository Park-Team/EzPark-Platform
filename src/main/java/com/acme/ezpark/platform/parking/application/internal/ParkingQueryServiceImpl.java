package com.acme.ezpark.platform.parking.application.internal;

import com.acme.ezpark.platform.parking.domain.model.aggregates.Parking;
import com.acme.ezpark.platform.parking.domain.model.queries.GetParkingByIdQuery;
import com.acme.ezpark.platform.parking.domain.model.queries.GetParkingsByLocationQuery;
import com.acme.ezpark.platform.parking.domain.model.queries.GetParkingsByOwnerIdQuery;
import com.acme.ezpark.platform.parking.domain.services.ParkingQueryService;
import com.acme.ezpark.platform.parking.infrastructure.persistence.jpa.repositories.ParkingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingQueryServiceImpl implements ParkingQueryService {

    private final ParkingRepository parkingRepository;

    public ParkingQueryServiceImpl(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Parking> handle(GetParkingByIdQuery query) {
        return parkingRepository.findById(query.parkingId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parking> handle(GetParkingsByLocationQuery query) {
        List<Parking> candidateParkings = parkingRepository.findParkingsWithinRadius(
            query.latitude(), 
            query.longitude(), 
            query.radiusKm()
        );

        return candidateParkings.stream()
            .filter(parking -> calculateDistance(
                query.latitude().doubleValue(), 
                query.longitude().doubleValue(),
                parking.getLatitude().doubleValue(), 
                parking.getLongitude().doubleValue()
            ) <= query.radiusKm().doubleValue())
            .sorted((p1, p2) -> {
                double dist1 = calculateDistance(
                    query.latitude().doubleValue(), query.longitude().doubleValue(),
                    p1.getLatitude().doubleValue(), p1.getLongitude().doubleValue()
                );
                double dist2 = calculateDistance(
                    query.latitude().doubleValue(), query.longitude().doubleValue(),
                    p2.getLatitude().doubleValue(), p2.getLongitude().doubleValue()
                );
                return Double.compare(dist1, dist2);
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parking> handle(GetParkingsByOwnerIdQuery query) {
        return parkingRepository.findByOwnerIdAndIsActiveTrue(query.ownerId());
    }
    

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}
