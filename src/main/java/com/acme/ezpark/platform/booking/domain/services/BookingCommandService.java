package com.acme.ezpark.platform.booking.domain.services;

import com.acme.ezpark.platform.booking.domain.model.aggregates.Booking;
import com.acme.ezpark.platform.booking.domain.model.commands.*;

import java.util.Optional;

public interface BookingCommandService {
    Optional<Booking> handle(CreateBookingCommand command);
    Optional<Booking> handle(CancelBookingCommand command);
    Optional<Booking> handle(ConfirmBookingCommand command);
    Optional<Booking> handle(StartBookingCommand command);
    Optional<Booking> handle(CompleteBookingCommand command);
}
