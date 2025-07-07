package com.acme.ezpark.platform.parking.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;

public record UpdateParkingResource(
    String address,
    BigDecimal width,
    BigDecimal length,
    BigDecimal height,
    BigDecimal pricePerHour,
    String description,
    String parkingType,
    List<String> imageUrls
) {
}
