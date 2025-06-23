package com.acme.ezpark.platform.review.domain.model.aggregates;

import com.acme.ezpark.platform.review.domain.model.valueobjects.Rating;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long parkingId;
    
    @Column(nullable = false)
    private Long bookingId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rating rating;
    
    @Column(length = 1000)
    private String comment;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    

    public Review(Long userId, Long parkingId, Long bookingId, Rating rating, String comment) {
        this.userId = userId;
        this.parkingId = parkingId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    

    public Long getId() {
        return id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getParkingId() {
        return parkingId;
    }
    
    public Long getBookingId() {
        return bookingId;
    }
    
    public Rating getRating() {
        return rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    

    public void updateReview(Rating newRating, String newComment) {
        this.rating = newRating;
        this.comment = newComment;
        this.updatedAt = new Date();
    }
    
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = new Date();
    }
    
    public boolean canBeUpdatedBy(Long requestingUserId) {
        return this.userId.equals(requestingUserId) && this.isActive;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
