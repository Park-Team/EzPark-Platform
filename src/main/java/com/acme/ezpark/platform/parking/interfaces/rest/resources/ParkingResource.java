package com.acme.ezpark.platform.parking.interfaces.rest.resources;

import java.math.BigDecimal;

public record ParkingResource(
    Long id,
    Long ownerId,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    BigDecimal width,
    BigDecimal length,
    BigDecimal height,
    BigDecimal pricePerHour,
    String description,
    String imageUrl,
    Boolean isAvailable,
    String parkingType
) {
}
