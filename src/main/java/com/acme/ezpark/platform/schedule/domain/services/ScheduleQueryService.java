package com.acme.ezpark.platform.schedule.domain.services;

import com.acme.ezpark.platform.schedule.domain.model.aggregates.Schedule;
import com.acme.ezpark.platform.schedule.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ScheduleQueryService {
    Optional<Schedule> handle(GetScheduleByIdQuery query);
    List<Schedule> handle(GetSchedulesByParkingIdQuery query);
    List<Schedule> handle(GetAllSchedulesQuery query);
}
