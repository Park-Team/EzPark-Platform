package com.acme.ezpark.platform.user.domain.model.commands;

public record UpdateUserProfilePictureCommand(Long userId, String profilePictureUrl) {
}
