package com.acme.ezpark.platform.parking.domain.model.commands;

import java.math.BigDecimal;

public record CreateParkingCommand(
    Long ownerId,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    BigDecimal width,
    BigDecimal length,
    BigDecimal height,
    BigDecimal pricePerHour,
    String description,
    String parkingType
) {
}
