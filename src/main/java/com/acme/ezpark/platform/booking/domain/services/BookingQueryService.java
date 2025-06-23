package com.acme.ezpark.platform.booking.domain.services;

import com.acme.ezpark.platform.booking.domain.model.aggregates.Booking;
import com.acme.ezpark.platform.booking.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface BookingQueryService {
    Optional<Booking> handle(GetBookingByIdQuery query);
    List<Booking> handle(GetBookingsByUserIdQuery query);
    List<Booking> handle(GetBookingsByUserIdAndStatusQuery query);
    List<Booking> handle(GetBookingsByParkingIdQuery query);
    List<Booking> handle(GetBookingsByParkingIdAndStatusQuery query);
    List<Booking> getAllBookings();
}
