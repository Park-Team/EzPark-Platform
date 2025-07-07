package com.acme.ezpark.platform.parking.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;

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
    List<String> imageUrls,
    Boolean isAvailable,
    String parkingType
) {
}
