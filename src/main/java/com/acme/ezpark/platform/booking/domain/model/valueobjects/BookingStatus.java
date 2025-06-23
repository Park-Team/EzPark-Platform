package com.acme.ezpark.platform.booking.domain.model.valueobjects;

public enum BookingStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    ACTIVE("Active"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }    public boolean canTransitionTo(BookingStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED -> newStatus == ACTIVE || newStatus == CANCELLED;
            case ACTIVE -> newStatus == COMPLETED || newStatus == CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}
