package com.acme.ezpark.platform.vehicle.domain.model.valueobjects;

public enum VehicleType {
    CAR("Car"),
    MOTORCYCLE("Motorcycle"),
    TRUCK("Truck"),
    VAN("Van"),
    SUV("SUV"),
    BUS("Bus");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
