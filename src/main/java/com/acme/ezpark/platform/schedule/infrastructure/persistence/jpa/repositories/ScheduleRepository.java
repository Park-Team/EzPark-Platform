package com.acme.ezpark.platform.schedule.infrastructure.persistence.jpa.repositories;

import com.acme.ezpark.platform.schedule.domain.model.aggregates.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByParkingIdAndIsActiveTrue(Long parkingId);
    List<Schedule> findByParkingId(Long parkingId);
    List<Schedule> findByIsActiveTrue();
}
