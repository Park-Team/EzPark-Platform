package com.acme.ezpark.platform.user.domain.model.aggregates;

import com.acme.ezpark.platform.user.domain.model.valueobjects.UserRole;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@NoArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false, length = 255)
    private String password;
    
    @Column(nullable = false, length = 100)
    private String firstName;
    
    @Column(nullable = false, length = 100)
    private String lastName;
    
    @Column(nullable = false, unique = true, length = 20)
    private String phone;
    
    @Column(nullable = false)
    private LocalDate birthDate;
      @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.GUEST; // Default role
    
    @Column(length = 255)
    private String profilePicture;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false)
    private Boolean isVerified = false;
    
    @Column(nullable = false)
    private LocalDate createdAt;
    
    @Column(nullable = false)
    private LocalDate updatedAt;
    

    public Long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public String getProfilePicture() {
        return profilePicture;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public Boolean getIsVerified() {
        return isVerified;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }    

    public User(String email, String password, String firstName, String lastName, String phone, LocalDate birthDate, UserRole role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.birthDate = birthDate;
        this.role = role;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }
    

    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void updateProfile(String firstName, String lastName, String phone, LocalDate birthDate, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.birthDate = birthDate;
        this.profilePicture = profilePicture;
        this.updatedAt = LocalDate.now();
    }
    
    public void verifyUser() {
        this.isVerified = true;
        this.updatedAt = LocalDate.now();
    }
    
    public void deactivateUser() {
        this.isActive = false;
        this.updatedAt = LocalDate.now();
    }
    
    public void activateUser() {
        this.isActive = true;
        this.updatedAt = LocalDate.now();
    }
    
    public void setProfilePicture(String profilePictureUrl) {
        this.profilePicture = profilePictureUrl;
        this.updatedAt = LocalDate.now();
    }
    

    public boolean isHost() {
        return role == UserRole.HOST || role == UserRole.BOTH;
    }
    
    public boolean isGuest() {
        return role == UserRole.GUEST || role == UserRole.BOTH;
    }
    
    public boolean canUpgradeToRole(UserRole newRole) {
        if (this.role == newRole) {
            return false;
        }
        return (this.role == UserRole.HOST && newRole == UserRole.GUEST) ||
               (this.role == UserRole.GUEST && newRole == UserRole.HOST);
    }
    
    public void upgradeRole(UserRole requestedRole) {
        if (canUpgradeToRole(requestedRole)) {
            this.role = UserRole.BOTH;
            this.updatedAt = LocalDate.now();
        }
    }
    
    public boolean canDowngradeFromRole(UserRole roleToRemove) {
        if (this.role == UserRole.BOTH) {
            return roleToRemove == UserRole.HOST || roleToRemove == UserRole.GUEST;
        }
        return this.role == roleToRemove;
    }
    
    public void downgradeFromRole(UserRole roleToRemove) {
        if (this.role == UserRole.BOTH) {
            if (roleToRemove == UserRole.HOST) {
                this.role = UserRole.GUEST;
            } else if (roleToRemove == UserRole.GUEST) {
                this.role = UserRole.HOST;
            }
            this.updatedAt = LocalDate.now();
        } else if (this.role == roleToRemove) {

            this.isActive = false;
            this.updatedAt = LocalDate.now();
        }
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}
