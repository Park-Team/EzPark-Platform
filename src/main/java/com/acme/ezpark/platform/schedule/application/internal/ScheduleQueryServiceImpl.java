package com.acme.ezpark.platform.schedule.application.internal;

import com.acme.ezpark.platform.schedule.domain.model.aggregates.Schedule;
import com.acme.ezpark.platform.schedule.domain.model.queries.*;
import com.acme.ezpark.platform.schedule.domain.services.ScheduleQueryService;
import com.acme.ezpark.platform.schedule.infrastructure.persistence.jpa.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleQueryServiceImpl implements ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleQueryServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Optional<Schedule> handle(GetScheduleByIdQuery query) {
        return scheduleRepository.findById(query.scheduleId());
    }

    @Override
    public List<Schedule> handle(GetSchedulesByParkingIdQuery query) {
        return scheduleRepository.findByParkingIdAndIsActiveTrue(query.parkingId());
    }

    @Override
    public List<Schedule> handle(GetAllSchedulesQuery query) {
        return scheduleRepository.findByIsActiveTrue();
    }
}
