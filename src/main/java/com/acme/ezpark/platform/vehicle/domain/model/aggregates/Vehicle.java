package com.acme.ezpark.platform.vehicle.domain.model.aggregates;

import com.acme.ezpark.platform.vehicle.domain.model.valueobjects.VehicleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 20)
    private String licensePlate;
    
    @Column(nullable = false, length = 100)
    private String brand;
    
    @Column(nullable = false, length = 100)
    private String model;
    
    @Column(length = 50)
    private String color;
      @Column
    private String year;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;
      @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;
    

    public Long getId() {
        return id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getYear() {
        return year;
    }
    
    public VehicleType getVehicleType() {
        return vehicleType;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public java.util.Date getCreatedAt() {
        return createdAt;
    }
    
    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }
    

    public Vehicle(Long userId, String licensePlate, String brand, String model, String color, String year, VehicleType vehicleType) {
        this.userId = userId;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.vehicleType = vehicleType;
        this.createdAt = new java.util.Date();
        this.updatedAt = new java.util.Date();
    }
    

    public String getVehicleDescription() {
        return String.format("%s %s %s (%s)", year, brand, model, licensePlate);
    }
      public void updateVehicle(String brand, String model, String color, String year, VehicleType vehicleType) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.vehicleType = vehicleType;
        this.updatedAt = new java.util.Date();
    }
    
    public void deactivateVehicle() {
        this.isActive = false;
        this.updatedAt = new java.util.Date();
    }
    
    public void deactivate() {
        this.deactivateVehicle();
    }
    
    public void activateVehicle() {
        this.isActive = true;
        this.updatedAt = new java.util.Date();
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = new java.util.Date();
        updatedAt = new java.util.Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new java.util.Date();
    }
}
