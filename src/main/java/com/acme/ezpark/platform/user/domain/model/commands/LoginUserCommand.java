package com.acme.ezpark.platform.user.domain.model.commands;

public record LoginUserCommand(
    String email,
    String password
) {
}
