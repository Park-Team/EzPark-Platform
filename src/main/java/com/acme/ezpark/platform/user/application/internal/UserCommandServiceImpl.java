package com.acme.ezpark.platform.user.application.internal;

import com.acme.ezpark.platform.user.domain.model.aggregates.User;
import com.acme.ezpark.platform.user.domain.model.commands.CreateUserCommand;
import com.acme.ezpark.platform.user.domain.model.commands.LoginUserCommand;
import com.acme.ezpark.platform.user.domain.model.commands.UpdateUserCommand;
import com.acme.ezpark.platform.user.domain.model.commands.UpgradeUserRoleCommand;
import com.acme.ezpark.platform.user.domain.model.commands.RemoveUserRoleCommand;
import com.acme.ezpark.platform.user.domain.model.commands.UpdateUserProfilePictureCommand;
import com.acme.ezpark.platform.user.domain.model.valueobjects.UserRole;
import com.acme.ezpark.platform.user.domain.services.UserCommandService;
import com.acme.ezpark.platform.user.infrastructure.persistence.jpa.repositories.UserRepository;
import com.acme.ezpark.platform.booking.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.acme.ezpark.platform.parking.infrastructure.persistence.jpa.repositories.ParkingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ParkingRepository parkingRepository;

    public UserCommandServiceImpl(UserRepository userRepository,
                                BookingRepository bookingRepository,
                                ParkingRepository parkingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.parkingRepository = parkingRepository;
    }

    @Override
    public Optional<User> handle(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            return Optional.empty();
        }


        var user = new User(
            command.email(),
            command.password(),
            command.firstName(),
            command.lastName(),
            command.phone(),
            command.birthDate(),
            command.role()
        );

        try {
            userRepository.save(user);
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> handle(UpdateUserCommand command) {
        System.out.println("=== UpdateUserCommand ===");
        System.out.println("UserId: " + command.userId());
        System.out.println("ProfilePicture in command: " + command.profilePicture());
        
        return userRepository.findById(command.userId())
            .map(user -> {
                System.out.println("Usuario encontrado: " + user.getEmail());
                System.out.println("Imagen anterior: " + user.getProfilePicture());
                
                user.updateProfile(
                    command.firstName(),
                    command.lastName(),
                    command.phone(),
                    command.birthDate(),
                    command.profilePicture()
                );
                
                User savedUser = userRepository.save(user);
                System.out.println("Imagen después de updateProfile: " + savedUser.getProfilePicture());
                
                return savedUser;
            });
    }
    
    @Override
    public Optional<User> handle(LoginUserCommand command) {
        return userRepository.findByEmail(command.email())
            .filter(user -> user.getPassword().equals(command.password())) // In production, use proper password verification
            .filter(user -> user.getIsActive());
    }
    
    @Override
    public Optional<User> handle(UpgradeUserRoleCommand command) {
        return userRepository.findByEmail(command.email())
            .map(user -> {
                user.upgradeRole(command.requestedRole());
                userRepository.save(user);
                return user;
            });
    }
    
    @Override
    @Transactional
    public Optional<User> handle(RemoveUserRoleCommand command) {
        return userRepository.findById(command.userId())
            .map(user -> {
                cleanupRoleSpecificData(user, command.roleToRemove());

                user.downgradeFromRole(command.roleToRemove());
                userRepository.save(user);
                return user;
            });
    }
    
    private void cleanupRoleSpecificData(User user, UserRole roleToRemove) {
        if (roleToRemove == UserRole.HOST) {
            cleanupHostData(user.getId());
        } else if (roleToRemove == UserRole.GUEST) {
            cleanupGuestData(user.getId());
        }
    }
    
    private void cleanupHostData(Long userId) {
        var myParkings = parkingRepository.findByOwnerId(userId);
        myParkings.forEach(parking -> {
            var bookings = bookingRepository.findByParkingId(parking.getId());
            bookings.forEach(booking -> {
                if (!booking.getStatus().name().equals("COMPLETED") && 
                    !booking.getStatus().name().equals("CANCELLED")) {
                    booking.cancel();
                    bookingRepository.save(booking);
                }
            });
        });
        

        myParkings.forEach(parking -> {
            parking.deactivate();
            parkingRepository.save(parking);
        });
    }
    
    private void cleanupGuestData(Long userId) {
        var myBookings = bookingRepository.findByUserId(userId);
        myBookings.forEach(booking -> {
            if (!booking.getStatus().name().equals("COMPLETED") && 
                !booking.getStatus().name().equals("CANCELLED")) {
                booking.cancel();
                bookingRepository.save(booking);
            }
        });
        // Note: Vehicle cleanup removed as vehicle module was eliminated
    }

    @Override
    public Optional<User> handle(UpdateUserProfilePictureCommand command) {
        System.out.println("=== UpdateUserProfilePictureCommand ===");
        System.out.println("UserId: " + command.userId());
        System.out.println("ProfilePictureUrl: " + command.profilePictureUrl());
        
        return userRepository.findById(command.userId())
            .map(user -> {
                System.out.println("Usuario encontrado: " + user.getEmail());
                System.out.println("Imagen anterior: " + user.getProfilePicture());
                
                user.setProfilePicture(command.profilePictureUrl());
                User savedUser = userRepository.save(user);
                
                System.out.println("Imagen nueva: " + savedUser.getProfilePicture());
                System.out.println("Usuario guardado exitosamente");
                
                return savedUser;
            });
    }
}
