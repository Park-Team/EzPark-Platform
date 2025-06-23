package com.acme.ezpark.platform.booking.interfaces.rest.resources;

import java.time.Instant;

public record BookingCancelInfoResource(
    Boolean canCancel,
    Long minutesUntilDeadline,
    Instant cancelDeadline,
    String message
) {
}
