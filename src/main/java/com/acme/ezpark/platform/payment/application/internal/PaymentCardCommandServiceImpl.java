package com.acme.ezpark.platform.payment.application.internal;

import com.acme.ezpark.platform.payment.domain.model.aggregates.PaymentCard;
import com.acme.ezpark.platform.payment.domain.model.commands.*;
import com.acme.ezpark.platform.payment.domain.services.PaymentCardCommandService;
import com.acme.ezpark.platform.payment.infrastructure.persistence.jpa.repositories.PaymentCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentCardCommandServiceImpl implements PaymentCardCommandService {

    private final PaymentCardRepository paymentCardRepository;

    public PaymentCardCommandServiceImpl(PaymentCardRepository paymentCardRepository) {
        this.paymentCardRepository = paymentCardRepository;
    }

    @Override
    public Optional<PaymentCard> handle(CreatePaymentCardCommand command) {
        if (paymentCardRepository.existsByCardNumberAndIsActiveTrue(command.cardNumber())) {
            return Optional.empty();
        }

        var paymentCard = new PaymentCard(
            command.userId(),
            command.cardholderName(),
            command.cardNumber(),
            command.expiryMonth(),
            command.expiryYear(),
            command.cvv(),
            command.cardType()
        );

        var existingCards = paymentCardRepository.findByUserIdAndIsActiveTrue(command.userId());
        if (existingCards.isEmpty()) {
            paymentCard.setAsDefault();
        }

        try {
            paymentCardRepository.save(paymentCard);
            return Optional.of(paymentCard);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<PaymentCard> handle(UpdatePaymentCardCommand command) {
        return paymentCardRepository.findById(command.cardId())
            .map(card -> {
                card.updateCard(
                    command.cardholderName(),
                    command.expiryMonth(),
                    command.expiryYear()
                );
                paymentCardRepository.save(card);
                return card;
            });
    }

    @Override
    public boolean handle(DeletePaymentCardCommand command) {
        return paymentCardRepository.findById(command.cardId())
            .map(card -> {
                card.deactivateCard();
                paymentCardRepository.save(card);
                return true;
            })
            .orElse(false);
    }    @Override
    @Transactional
    public Optional<PaymentCard> handle(SetDefaultPaymentCardCommand command) {
        return paymentCardRepository.findById(command.cardId())
            .map(card -> {
                var userCards = paymentCardRepository.findByUserIdAndIsActiveTrue(card.getUserId());
                userCards.forEach(PaymentCard::removeDefault);
                paymentCardRepository.saveAll(userCards);

                card.setAsDefault();
                paymentCardRepository.save(card);
                return card;
            });
    }

    @Override
    public void deletePaymentCard(Long paymentCardId) {
        paymentCardRepository.deleteById(paymentCardId);
    }
}
