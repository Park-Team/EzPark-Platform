package com.acme.ezpark.platform.user.interfaces.rest.resources;

import com.acme.ezpark.platform.user.domain.model.valueobjects.UserRole;


public record RemoveUserRoleResource(
    Long userId,
    UserRole roleToRemove
) {
}
