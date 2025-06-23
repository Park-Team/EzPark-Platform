package com.acme.ezpark.platform.schedule.interfaces.rest.resources;

import java.time.LocalTime;

public record ScheduleResource(
        Long id,
        Long parkingId,
        String dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        Boolean isActive
) {
}
