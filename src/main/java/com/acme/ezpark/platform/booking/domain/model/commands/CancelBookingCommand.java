package com.acme.ezpark.platform.booking.domain.model.commands;

public record CancelBookingCommand(
    Long bookingId,
    String cancellationReason
) {
}
