package com.acme.ezpark.platform.booking.domain.model.commands;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateBookingCommand(
    Long userId,
    Long parkingId,
    Long vehicleId,
    Instant startTime,
    Instant endTime,
    String notes
) {
}
