package com.acme.ezpark.platform.parking.domain.model.queries;

import java.math.BigDecimal;

public record GetParkingsByLocationQuery(
    BigDecimal latitude,
    BigDecimal longitude,
    BigDecimal radiusKm
) {
}
