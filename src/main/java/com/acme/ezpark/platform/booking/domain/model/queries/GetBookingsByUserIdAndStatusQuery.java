package com.acme.ezpark.platform.booking.domain.model.queries;

import com.acme.ezpark.platform.booking.domain.model.valueobjects.BookingStatus;

public record GetBookingsByUserIdAndStatusQuery(
    Long userId,
    BookingStatus status
) {
}
