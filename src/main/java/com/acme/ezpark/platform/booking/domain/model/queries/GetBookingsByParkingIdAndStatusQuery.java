package com.acme.ezpark.platform.booking.domain.model.queries;

import com.acme.ezpark.platform.booking.domain.model.valueobjects.BookingStatus;

public record GetBookingsByParkingIdAndStatusQuery(Long parkingId, BookingStatus status) {
}
