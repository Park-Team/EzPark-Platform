package com.acme.ezpark.platform.payment.domain.model.commands;

import com.acme.ezpark.platform.payment.domain.model.valueobjects.CardType;

public record CreatePaymentCardCommand(
    Long userId,
    String cardholderName,
    String cardNumber,
    String expiryMonth,
    String expiryYear,
    String cvv,
    CardType cardType
) {
}
