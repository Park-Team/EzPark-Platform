package com.acme.ezpark.platform.schedule.interfaces.rest.transform;

import com.acme.ezpark.platform.schedule.domain.model.commands.CreateScheduleCommand;
import com.acme.ezpark.platform.schedule.interfaces.rest.resources.CreateScheduleResource;

public class CreateScheduleCommandFromResourceAssembler {
    
    public static CreateScheduleCommand toCommandFromResource(CreateScheduleResource resource) {
        return new CreateScheduleCommand(
                resource.parkingId(),
                resource.dayOfWeek(),
                resource.startTime(),
                resource.endTime()
        );
    }
}
