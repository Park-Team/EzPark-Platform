package com.acme.ezpark.platform.parking.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;

public record CreateParkingResource(
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    BigDecimal width,
    BigDecimal length,
    BigDecimal height,
    BigDecimal pricePerHour,
    String description,
    String parkingType,
    List<String> imageUrls
) {
}
