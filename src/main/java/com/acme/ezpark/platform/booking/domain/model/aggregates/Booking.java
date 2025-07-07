package com.acme.ezpark.platform.booking.domain.model.aggregates;

import com.acme.ezpark.platform.booking.domain.model.valueobjects.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long parkingId;
    
    @Column(nullable = false)
    private Instant startTime;
    
    @Column(nullable = false)
    private Instant endTime;
    
    @Column
    private Instant actualStartTime;
    
    @Column
    private Instant actualEndTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
      @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    
    @Column(length = 1000)
    private String notes;
    
    @Column(length = 500)
    private String cancellationReason;
      @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getParkingId() {
        return parkingId;
    }
      public Instant getStartTime() {
        return startTime;
    }
    
    public Instant getEndTime() {
        return endTime;
    }
    
    public Instant getActualStartTime() {
        return actualStartTime;
    }
    
    public Instant getActualEndTime() {
        return actualEndTime;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
      public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public String getCancellationReason() {
        return cancellationReason;
    }
    
    public java.util.Date getCreatedAt() {
        return createdAt;
    }
    
    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }    // Constructor
    public Booking(Long userId, Long parkingId, Instant startTime, 
                  Instant endTime, BigDecimal pricePerHour, String notes) {
        this.userId = userId;
        this.parkingId = parkingId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = calculateTotalPrice(startTime, endTime, pricePerHour);
        this.notes = notes;
        this.createdAt = new java.util.Date();
        this.updatedAt = new java.util.Date();
    }
      // Business methods
    private BigDecimal calculateTotalPrice(Instant startTime, Instant endTime, BigDecimal pricePerHour) {
        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        // If less than 1 hour, charge for 1 hour minimum
        if (hours == 0) {
            hours = 1;
        }
        return pricePerHour.multiply(BigDecimal.valueOf(hours));
    }
    
    public long getBookedHours() {
        return ChronoUnit.HOURS.between(startTime, endTime);
    }
    
    public long getActualHours() {
        if (actualStartTime != null && actualEndTime != null) {
            return ChronoUnit.HOURS.between(actualStartTime, actualEndTime);
        }
        return 0;
    }
    
    public void confirmBooking() {
        if (status == BookingStatus.PENDING) {
            this.status = BookingStatus.CONFIRMED;
            this.updatedAt = new java.util.Date();
        }
    }    public void startBooking() {
        if (status == BookingStatus.CONFIRMED) {
            this.status = BookingStatus.ACTIVE;
            this.actualStartTime = Instant.now();
            this.updatedAt = new java.util.Date();
        }
    }
      public void completeBooking() {
        if (status == BookingStatus.ACTIVE) {
            this.status = BookingStatus.COMPLETED;
            this.actualEndTime = Instant.now();
            this.updatedAt = new java.util.Date();
        }
    }
    
    public void cancelBooking(String reason) {
        if (status == BookingStatus.PENDING || status == BookingStatus.CONFIRMED) {
            this.status = BookingStatus.CANCELLED;
            this.cancellationReason = reason;
            this.updatedAt = new java.util.Date();
        }
    }
    
    public void cancel() {
        this.cancelBooking("User role removal - automatic cancellation");
    }
      public boolean isActive() {
        return status == BookingStatus.CONFIRMED || status == BookingStatus.ACTIVE;
    }
    
    public boolean isPast() {
        return endTime.isBefore(Instant.now()) || status == BookingStatus.COMPLETED;
    }
    
    public boolean isFuture() {
        return startTime.isAfter(Instant.now()) && status != BookingStatus.CANCELLED;
    }

    

    public boolean canBeCancelled() {
        if (status != BookingStatus.PENDING) {
            return false;
        }
        Instant cancelDeadline = startTime.minus(15, ChronoUnit.MINUTES);
        return Instant.now().isBefore(cancelDeadline);
    }
    

    public long getMinutesUntilCancelDeadline() {
        if (!canBeCancelled()) {
            return 0;
        }
        Instant cancelDeadline = startTime.minus(15, ChronoUnit.MINUTES);
        return ChronoUnit.MINUTES.between(Instant.now(), cancelDeadline);
    }
    

    public boolean shouldBeConfirmed() {
        if (status != BookingStatus.PENDING) {
            return false;
        }
        Instant confirmTime = startTime.minus(15, ChronoUnit.MINUTES);
        return Instant.now().isAfter(confirmTime);
    }
    

    public boolean shouldBeActive() {
        if (status != BookingStatus.CONFIRMED) {
            return false;
        }
        Instant now = Instant.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
    }    

    public boolean shouldBeCompleted() {
        if (status != BookingStatus.ACTIVE) {
            return false;
        }
        return Instant.now().isAfter(endTime);
    }
    

    public void updateStatusBasedOnTime() {
        if (shouldBeCompleted()) {
            this.status = BookingStatus.COMPLETED;
            this.actualEndTime = Instant.now();
            this.updatedAt = new java.util.Date();        } else if (shouldBeActive()) {
            this.status = BookingStatus.ACTIVE;
            if (this.actualStartTime == null) {
                this.actualStartTime = Instant.now();
            }
            this.updatedAt = new java.util.Date();
        } else if (shouldBeConfirmed()) {
            this.status = BookingStatus.CONFIRMED;
            this.updatedAt = new java.util.Date();
        }
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
