package com.acme.ezpark.platform.user.interfaces.rest.resources;

import com.acme.ezpark.platform.user.domain.model.valueobjects.UserRole;
import java.time.LocalDate;

public record UserResource(
    Long id,
    String email,
    String firstName,
    String lastName,
    String phone,
    LocalDate birthDate,
    UserRole role,
    String profilePicture,
    Boolean isActive,
    Boolean isVerified
) {
}
