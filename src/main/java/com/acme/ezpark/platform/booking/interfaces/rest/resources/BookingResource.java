package com.acme.ezpark.platform.booking.interfaces.rest.resources;

import com.acme.ezpark.platform.booking.domain.model.valueobjects.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record BookingResource(
    Long id,
    Long userId,
    Long parkingId,
    Instant startTime,
    Instant endTime,
    Instant actualStartTime,
    Instant actualEndTime,
    BookingStatus status,
    BigDecimal totalPrice,
    String notes,
    String cancellationReason
) {
}
