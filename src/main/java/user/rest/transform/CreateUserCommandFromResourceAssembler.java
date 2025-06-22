package com.acme.ezpark.platform.user.interfaces.rest.transform;

import com.acme.ezpark.platform.user.domain.model.commands.CreateUserCommand;
import com.acme.ezpark.platform.user.domain.model.valueobjects.UserRole;
import com.acme.ezpark.platform.user.interfaces.rest.resources.CreateUserResource;

public class CreateUserCommandFromResourceAssembler {
    public static CreateUserCommand toCommandFromResource(CreateUserResource resource) {
        return new CreateUserCommand(
            resource.email(),
            resource.password(),
            resource.firstName(),
            resource.lastName(),
            resource.phone(),
            resource.birthDate(),
            resource.role()
        );
    }
}
