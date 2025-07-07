package com.acme.ezpark.platform.booking.interfaces.rest.transform;

import com.acme.ezpark.platform.booking.domain.model.aggregates.Booking;
import com.acme.ezpark.platform.booking.interfaces.rest.resources.BookingResource;

public class BookingResourceFromEntityAssembler {    public static BookingResource toResourceFromEntity(Booking entity) {
        return new BookingResource(
            entity.getId(),
            entity.getUserId(),
            entity.getParkingId(),
            entity.getStartTime(),
            entity.getEndTime(),
            entity.getActualStartTime(),
            entity.getActualEndTime(),
            entity.getStatus(),
            entity.getTotalPrice(),
            entity.getNotes(),
            entity.getCancellationReason()
        );
    }
}
