package com.acme.ezpark.platform.user.interfaces.rest.transform;

import com.acme.ezpark.platform.user.domain.model.aggregates.User;
import com.acme.ezpark.platform.user.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User entity) {
        return new UserResource(
            entity.getId(),
            entity.getEmail(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getPhone(),
            entity.getBirthDate(),
            entity.getRole(),
            entity.getProfilePicture(),
            entity.getIsActive(),
            entity.getIsVerified()
        );
    }
}
