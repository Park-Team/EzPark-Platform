package com.acme.ezpark.platform.user.interfaces.rest.resources;

public record LoginUserResource(
    String email,
    String password
) {
}
