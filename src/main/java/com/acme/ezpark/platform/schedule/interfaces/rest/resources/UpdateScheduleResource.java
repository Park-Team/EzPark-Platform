package com.acme.ezpark.platform.schedule.interfaces.rest.resources;

import java.time.LocalTime;

public record UpdateScheduleResource(
        String dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {
}
