package com.acme.ezpark.platform.payment.interfaces.rest.resources;

public record PaymentCardResource(
        Long id,
        Long userId,
        String cardNumber,
        String cardholderName,
        String expiryMonth,
        String expiryYear,
        String cardType,
        Boolean isDefault,
        Boolean isActive
) {
}
