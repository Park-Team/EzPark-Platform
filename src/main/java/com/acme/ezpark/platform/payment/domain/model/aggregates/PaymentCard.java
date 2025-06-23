package com.acme.ezpark.platform.payment.domain.model.aggregates;

import com.acme.ezpark.platform.payment.domain.model.valueobjects.CardType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_cards")
@Getter
@Setter
@NoArgsConstructor
public class PaymentCard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 100)
    private String cardholderName;
    
    @Column(nullable = false, length = 20)
    private String cardNumber; // Should be encrypted in production
    
    @Column(nullable = false, length = 5)
    private String expiryMonth;
    
    @Column(nullable = false, length = 5)
    private String expiryYear;
    
    @Column(nullable = false, length = 10)
    private String cvv; // Should be encrypted in production
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardType cardType;
    
    @Column(nullable = false)
    private Boolean isDefault = false;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;
      @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;

    public PaymentCard(Long userId, String cardholderName, String cardNumber, String expiryMonth, 
                      String expiryYear, String cvv, CardType cardType) {
        this.userId = userId;
        this.cardholderName = cardholderName;
        this.cardNumber = cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
        this.cardType = cardType;
        this.createdAt = new java.util.Date();
        this.updatedAt = new java.util.Date();
    }
    

    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
    
    public boolean isExpired() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int currentYear = cal.get(java.util.Calendar.YEAR);
        int currentMonth = cal.get(java.util.Calendar.MONTH) + 1; // Calendar months are 0-based
        
        int cardYear = Integer.parseInt(expiryYear);
        int cardMonth = Integer.parseInt(expiryMonth);
        
        return cardYear < currentYear || (cardYear == currentYear && cardMonth < currentMonth);
    }
    
    public void setAsDefault() {
        this.isDefault = true;
        this.updatedAt = new java.util.Date();
    }
    
    public void removeDefault() {
        this.isDefault = false;
        this.updatedAt = new java.util.Date();
    }
    
    public void updateCard(String cardholderName, String expiryMonth, String expiryYear) {
        this.cardholderName = cardholderName;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.updatedAt = new java.util.Date();
    }
    
    public void deactivateCard() {
        this.isActive = false;
        this.isDefault = false;
        this.updatedAt = new java.util.Date();
    }
    
    public void activateCard() {
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
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public String getCardholderName() {
        return cardholderName;
    }
    
    public String getExpiryMonth() {
        return expiryMonth;
    }
    
    public String getExpiryYear() {
        return expiryYear;
    }
    
    public String getCvv() {
        return cvv;
    }
    
    public CardType getCardType() {
        return cardType;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
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
}
