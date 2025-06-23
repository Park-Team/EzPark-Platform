package com.acme.ezpark.platform.payment.domain.services;

import com.acme.ezpark.platform.payment.domain.model.aggregates.PaymentCard;
import com.acme.ezpark.platform.payment.domain.model.commands.*;

import java.util.Optional;

public interface PaymentCardCommandService {
    Optional<PaymentCard> handle(CreatePaymentCardCommand command);
    Optional<PaymentCard> handle(UpdatePaymentCardCommand command);
    boolean handle(DeletePaymentCardCommand command);
    Optional<PaymentCard> handle(SetDefaultPaymentCardCommand command);
    void deletePaymentCard(Long paymentCardId);
}
