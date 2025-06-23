package com.acme.ezpark.platform.booking.domain.model.commands;

public record CompleteBookingCommand(
    Long bookingId
) {
}
