package com.acme.ezpark.platform.payment.domain.model.commands;

public record UpdatePaymentCardCommand(
    Long cardId,
    String cardholderName,
    String expiryMonth,
    String expiryYear
) {
}
