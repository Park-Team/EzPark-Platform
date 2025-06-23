package com.acme.ezpark.platform.payment.interfaces.rest.transform;

import com.acme.ezpark.platform.payment.domain.model.aggregates.PaymentCard;
import com.acme.ezpark.platform.payment.interfaces.rest.resources.CreatePaymentCardResource;
import com.acme.ezpark.platform.payment.interfaces.rest.resources.PaymentCardResource;

public class PaymentCardResourceFromEntityAssembler {      public static PaymentCardResource toResourceFromEntity(PaymentCard entity) {
        return new PaymentCardResource(
                entity.getId(),
                entity.getUserId(),
                entity.getCardNumber(),
                entity.getCardholderName(),
                entity.getExpiryMonth(),
                entity.getExpiryYear(),
                entity.getCardType().toString(),
                entity.getIsDefault(),
                entity.getIsActive()
        );
    }
}
