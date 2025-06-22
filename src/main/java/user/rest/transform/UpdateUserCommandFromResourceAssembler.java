package com.acme.ezpark.platform.user.interfaces.rest.transform;

import com.acme.ezpark.platform.user.domain.model.commands.UpdateUserCommand;
import com.acme.ezpark.platform.user.interfaces.rest.resources.UpdateUserResource;

public class UpdateUserCommandFromResourceAssembler {
    public static UpdateUserCommand toCommandFromResource(Long userId, UpdateUserResource resource) {
        return new UpdateUserCommand(
            userId,
            resource.firstName(),
            resource.lastName(),
            resource.phone(),
            resource.birthDate(),
            resource.profilePicture()
        );
    }
}
