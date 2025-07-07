package com.acme.ezpark.platform.user.domain.services;

import com.acme.ezpark.platform.user.domain.model.aggregates.User;
import com.acme.ezpark.platform.user.domain.model.commands.CreateUserCommand;
import com.acme.ezpark.platform.user.domain.model.commands.LoginUserCommand;
import com.acme.ezpark.platform.user.domain.model.commands.UpdateUserCommand;
import com.acme.ezpark.platform.user.domain.model.commands.UpdateUserProfilePictureCommand;
import com.acme.ezpark.platform.user.domain.model.commands.UpgradeUserRoleCommand;
import com.acme.ezpark.platform.user.domain.model.commands.RemoveUserRoleCommand;

import java.util.Optional;

public interface UserCommandService {
    Optional<User> handle(CreateUserCommand command);
    Optional<User> handle(UpdateUserCommand command);
    Optional<User> handle(UpdateUserProfilePictureCommand command);
    Optional<User> handle(LoginUserCommand command);
    Optional<User> handle(UpgradeUserRoleCommand command);
    Optional<User> handle(RemoveUserRoleCommand command);
}
