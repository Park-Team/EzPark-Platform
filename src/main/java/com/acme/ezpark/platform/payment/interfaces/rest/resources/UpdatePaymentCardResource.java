package com.acme.ezpark.platform.payment.interfaces.rest.resources;

public record UpdatePaymentCardResource(
    String cardholderName,
    String expiryMonth,
    String expiryYear
) {
}
