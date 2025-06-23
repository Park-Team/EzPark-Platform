package com.acme.ezpark.platform.payment.interfaces.rest.transform;

import com.acme.ezpark.platform.payment.domain.model.commands.CreatePaymentCardCommand;
import com.acme.ezpark.platform.payment.interfaces.rest.resources.CreatePaymentCardResource;

public class CreatePaymentCardCommandFromResourceAssembler {
    
    public static CreatePaymentCardCommand toCommandFromResource(CreatePaymentCardResource resource) {        return new CreatePaymentCardCommand(
                resource.userId(),
                resource.cardholderName(),
                resource.cardNumber(),
                resource.expiryMonth(),
                resource.expiryYear(),
                resource.cvv(),
                resource.cardType()
        );
    }
}
