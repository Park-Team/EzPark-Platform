package com.acme.ezpark.platform.payment.infrastructure.persistence.jpa.repositories;

import com.acme.ezpark.platform.payment.domain.model.aggregates.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {
    List<PaymentCard> findByUserIdAndIsActiveTrue(Long userId);
    Optional<PaymentCard> findByUserIdAndIsDefaultTrueAndIsActiveTrue(Long userId);
    boolean existsByCardNumberAndIsActiveTrue(String cardNumber);
}
