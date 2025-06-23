package com.acme.ezpark.platform.schedule.interfaces.rest.transform;

import com.acme.ezpark.platform.schedule.domain.model.aggregates.Schedule;
import com.acme.ezpark.platform.schedule.interfaces.rest.resources.ScheduleResource;

public class ScheduleResourceFromEntityAssembler {
    
    public static ScheduleResource toResourceFromEntity(Schedule entity) {
        return new ScheduleResource(
                entity.getId(),
                entity.getParkingId(),
                entity.getDayOfWeek(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getIsActive()
        );
    }
}
