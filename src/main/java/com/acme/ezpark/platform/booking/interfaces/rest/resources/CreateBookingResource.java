package com.acme.ezpark.platform.booking.interfaces.rest.resources;

import java.time.Instant;

public record CreateBookingResource(
    Long parkingId,
    Long vehicleId,
    Instant startTime,
    Instant endTime,
    String notes
) {
}
