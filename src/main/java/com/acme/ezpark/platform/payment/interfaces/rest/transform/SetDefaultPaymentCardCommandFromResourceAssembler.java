package com.acme.ezpark.platform.payment.interfaces.rest.transform;

import com.acme.ezpark.platform.payment.domain.model.commands.SetDefaultPaymentCardCommand;

public class SetDefaultPaymentCardCommandFromResourceAssembler {
    
    public static SetDefaultPaymentCardCommand toCommandFromResource(Long cardId) {
        return new SetDefaultPaymentCardCommand(cardId);
    }
}
