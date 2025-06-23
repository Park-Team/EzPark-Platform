package com.acme.ezpark.platform.user.interfaces.rest.transform;

import com.acme.ezpark.platform.user.domain.model.commands.LoginUserCommand;
import com.acme.ezpark.platform.user.interfaces.rest.resources.LoginUserResource;

public class LoginUserCommandFromResourceAssembler {
    public static LoginUserCommand toCommandFromResource(LoginUserResource resource) {
        return new LoginUserCommand(
            resource.email(),
            resource.password()
        );
    }
}
