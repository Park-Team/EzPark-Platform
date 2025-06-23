package com.acme.ezpark.platform.user.interfaces.rest.resources;

import java.time.LocalDate;

public record UpdateUserResource(
    String firstName,
    String lastName,
    String phone,
    LocalDate birthDate,
    String profilePicture
) {
}
