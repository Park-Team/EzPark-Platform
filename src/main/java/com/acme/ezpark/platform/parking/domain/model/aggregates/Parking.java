package com.acme.ezpark.platform.parking.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parkings")
@Getter
@Setter
@NoArgsConstructor
public class Parking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long ownerId;
    
    @Column(nullable = false, length = 255)
    private String address;
    
    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal width;
    
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal length;
    
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal height;
    
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal pricePerHour;
    
    @Column(length = 1000)
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "parking_images", joinColumns = @JoinColumn(name = "parking_id"))
    @Column(name = "image_url", length = 500)
    private List<String> imageUrls = new ArrayList<>();
    
    @Column(nullable = false)
    private Boolean isAvailable = true;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(length = 50)
    private String parkingType; // Covered, Uncovered, Garage
    
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;

    public Parking(Long ownerId, String address, BigDecimal latitude, BigDecimal longitude, 
                  BigDecimal width, BigDecimal length, BigDecimal height, BigDecimal pricePerHour,
                  String description, String parkingType, List<String> imageUrls) {
        this.ownerId = ownerId;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.width = width;
        this.length = length;
        this.height = height;
        this.pricePerHour = pricePerHour;
        this.description = description;
        this.parkingType = parkingType;
        this.imageUrls = imageUrls != null ? new ArrayList<>(imageUrls) : new ArrayList<>();
        this.createdAt = new java.util.Date();
        this.updatedAt = new java.util.Date();
    }
    

    public Long getId() {
        return id;
    }
    
    public Long getOwnerId() {
        return ownerId;
    }
    
    public String getAddress() {
        return address;
    }
    
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    public BigDecimal getLongitude() {
        return longitude;
    }
    
    public BigDecimal getWidth() {
        return width;
    }
    
    public BigDecimal getLength() {
        return length;
    }
    
    public BigDecimal getHeight() {
        return height;
    }
    
    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<String> getImageUrls() {
        return imageUrls;
    }
    
    public void addImageUrl(String imageUrl) {
        if (imageUrls.size() < 3) {
            imageUrls.add(imageUrl);
        }
    }
    
    public void removeImageUrl(String imageUrl) {
        imageUrls.remove(imageUrl);
    }
    
    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public String getParkingType() {
        return parkingType;
    }
    
    public java.util.Date getCreatedAt() {
        return createdAt;
    }
    
    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }
    

    public BigDecimal calculateDistance(BigDecimal userLat, BigDecimal userLng) {

        double lat1Rad = Math.toRadians(latitude.doubleValue());
        double lat2Rad = Math.toRadians(userLat.doubleValue());
        double deltaLatRad = Math.toRadians(userLat.subtract(latitude).doubleValue());
        double deltaLngRad = Math.toRadians(userLng.subtract(longitude).doubleValue());
        
        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLngRad / 2) * Math.sin(deltaLngRad / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = 6371 * c;
        
        return BigDecimal.valueOf(distance);
    }
    
    public void updateParking(String address, BigDecimal width, BigDecimal length, BigDecimal height,
                             BigDecimal pricePerHour, String description, String parkingType) {
        this.address = address;
        this.width = width;
        this.length = length;
        this.height = height;
        this.pricePerHour = pricePerHour;
        this.description = description;
        this.parkingType = parkingType;
        this.updatedAt = new java.util.Date();
    }
    
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
        this.updatedAt = new java.util.Date();
    }
    
    public void deactivateParking() {
        this.isActive = false;
        this.updatedAt = new java.util.Date();
    }
    
    public void deactivate() {
        this.deactivateParking();
    }
    
    public void activateParking() {
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
