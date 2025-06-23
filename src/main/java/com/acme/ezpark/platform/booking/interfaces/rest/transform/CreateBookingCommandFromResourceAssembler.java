package com.acme.ezpark.platform.booking.interfaces.rest.transform;

import com.acme.ezpark.platform.booking.domain.model.commands.CreateBookingCommand;
import com.acme.ezpark.platform.booking.interfaces.rest.resources.CreateBookingResource;

public class CreateBookingCommandFromResourceAssembler {    public static CreateBookingCommand toCommandFromResource(Long userId, CreateBookingResource resource) {
        return new CreateBookingCommand(
            userId,
            resource.parkingId(),
            resource.vehicleId(),
            resource.startTime(),
            resource.endTime(),
            resource.notes()
        );
    }
}
