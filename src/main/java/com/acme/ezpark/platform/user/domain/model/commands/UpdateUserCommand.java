package com.acme.ezpark.platform.user.domain.model.commands;

import java.time.LocalDate;

public record UpdateUserCommand(
    Long userId,
    String firstName,
    String lastName,
    String phone,
    LocalDate birthDate,
    String profilePicture
) {
}
