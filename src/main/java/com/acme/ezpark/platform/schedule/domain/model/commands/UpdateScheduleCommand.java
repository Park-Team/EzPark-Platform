package com.acme.ezpark.platform.schedule.domain.model.commands;

import java.time.LocalTime;

public record UpdateScheduleCommand(
        Long scheduleId,
        String dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {
}
