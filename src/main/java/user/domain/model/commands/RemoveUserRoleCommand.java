package com.acme.ezpark.platform.user.domain.model.commands;

import com.acme.ezpark.platform.user.domain.model.valueobjects.UserRole;


public record RemoveUserRoleCommand(
    Long userId,
    UserRole roleToRemove
) {
}
