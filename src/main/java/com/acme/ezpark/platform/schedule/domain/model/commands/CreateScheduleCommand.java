package com.acme.ezpark.platform.schedule.domain.model.commands;

import java.time.LocalTime;

public record CreateScheduleCommand(
        Long parkingId,
        String dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {
}
