package com.acme.ezpark.platform.schedule.interfaces.rest.transform;

import com.acme.ezpark.platform.schedule.domain.model.commands.UpdateScheduleCommand;
import com.acme.ezpark.platform.schedule.interfaces.rest.resources.UpdateScheduleResource;

public class UpdateScheduleCommandFromResourceAssembler {
    
    public static UpdateScheduleCommand toCommandFromResource(Long scheduleId, UpdateScheduleResource resource) {
        return new UpdateScheduleCommand(
                scheduleId,
                resource.dayOfWeek(),
                resource.startTime(),
                resource.endTime()
        );
    }
}
