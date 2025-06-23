package com.acme.ezpark.platform.payment.domain.services;

import com.acme.ezpark.platform.payment.domain.model.aggregates.PaymentCard;
import com.acme.ezpark.platform.payment.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface PaymentCardQueryService {
    Optional<PaymentCard> handle(GetPaymentCardByIdQuery query);
    List<PaymentCard> handle(GetPaymentCardsByUserIdQuery query);
}
