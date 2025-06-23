package com.acme.ezpark.platform.payment.interfaces.rest.transform;

import com.acme.ezpark.platform.payment.domain.model.commands.UpdatePaymentCardCommand;
import com.acme.ezpark.platform.payment.interfaces.rest.resources.UpdatePaymentCardResource;

public class UpdatePaymentCardCommandFromResourceAssembler {
    
    public static UpdatePaymentCardCommand toCommandFromResource(Long cardId, UpdatePaymentCardResource resource) {
        return new UpdatePaymentCardCommand(
                cardId,
                resource.cardholderName(),
                resource.expiryMonth(),
                resource.expiryYear()
        );
    }
}
