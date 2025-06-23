package com.acme.ezpark.platform.payment.application.internal;

import com.acme.ezpark.platform.payment.domain.model.aggregates.PaymentCard;
import com.acme.ezpark.platform.payment.domain.model.queries.*;
import com.acme.ezpark.platform.payment.domain.services.PaymentCardQueryService;
import com.acme.ezpark.platform.payment.infrastructure.persistence.jpa.repositories.PaymentCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentCardQueryServiceImpl implements PaymentCardQueryService {

    private final PaymentCardRepository paymentCardRepository;

    public PaymentCardQueryServiceImpl(PaymentCardRepository paymentCardRepository) {
        this.paymentCardRepository = paymentCardRepository;
    }

    @Override
    public Optional<PaymentCard> handle(GetPaymentCardByIdQuery query) {
        return paymentCardRepository.findById(query.cardId());
    }

    @Override
    public List<PaymentCard> handle(GetPaymentCardsByUserIdQuery query) {
        return paymentCardRepository.findByUserIdAndIsActiveTrue(query.userId());
    }
}
