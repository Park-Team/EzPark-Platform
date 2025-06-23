package com.acme.ezpark.platform.schedule.domain.services;

import com.acme.ezpark.platform.schedule.domain.model.aggregates.Schedule;
import com.acme.ezpark.platform.schedule.domain.model.commands.*;

import java.util.Optional;

public interface ScheduleCommandService {
    Optional<Schedule> handle(CreateScheduleCommand command);
    Optional<Schedule> handle(UpdateScheduleCommand command);
    boolean handle(DeleteScheduleCommand command);
    void deleteSchedule(Long scheduleId);
}
