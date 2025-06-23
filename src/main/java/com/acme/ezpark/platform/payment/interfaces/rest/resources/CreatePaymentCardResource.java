package com.acme.ezpark.platform.payment.interfaces.rest.resources;

import com.acme.ezpark.platform.payment.domain.model.valueobjects.CardType;

public record CreatePaymentCardResource(
    Long userId,
    String cardholderName,
    String cardNumber,
    String expiryMonth,
    String expiryYear,
    String cvv,
    CardType cardType
) {
}
