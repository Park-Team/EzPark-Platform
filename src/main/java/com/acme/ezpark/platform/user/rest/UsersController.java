package com.acme.ezpark.platform.user.interfaces.rest;

import com.acme.ezpark.platform.user.domain.model.queries.GetAllUsersQuery;
import com.acme.ezpark.platform.user.domain.model.queries.GetUserByIdQuery;
import com.acme.ezpark.platform.user.domain.model.queries.GetUserByEmailQuery;
import com.acme.ezpark.platform.user.domain.model.commands.RemoveUserRoleCommand;
import com.acme.ezpark.platform.user.domain.model.commands.UpdateUserProfilePictureCommand;
import com.acme.ezpark.platform.user.domain.model.valueobjects.UserRole;
import com.acme.ezpark.platform.user.domain.services.UserCommandService;
import com.acme.ezpark.platform.user.domain.services.UserQueryService;
import com.acme.ezpark.platform.user.interfaces.rest.resources.*;
import com.acme.ezpark.platform.user.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User Management Endpoints")
public class UsersController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UsersController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user in the system")
    public ResponseEntity<UserResource> createUser(@RequestBody CreateUserResource resource) {
        var createUserCommand = CreateUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(createUserCommand);
        
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user credentials")
    public ResponseEntity<UserResource> loginUser(@RequestBody LoginUserResource resource) {
        var loginUserCommand = LoginUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(loginUserCommand);
        
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Get all users in the system")
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var getAllUsersQuery = new GetAllUsersQuery();
        var users = userQueryService.handle(getAllUsersQuery);
        
        var userResources = users.stream()
            .map(UserResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
            
        return ResponseEntity.ok(userResources);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Get user information by user ID")
    public ResponseEntity<UserResource> getUserById(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @GetMapping("/by-email/{email}")
    @Operation(summary = "Get user by email", description = "Get user information by email address")
    public ResponseEntity<UserResource> getUserByEmail(
            @Parameter(description = "User email") @PathVariable String email) {
        var getUserByEmailQuery = new GetUserByEmailQuery(email);
        var user = userQueryService.handle(getUserByEmailQuery);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile", description = "Update user profile information")
    public ResponseEntity<UserResource> updateUser(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestBody UpdateUserResource resource) {
        
        System.out.println("=== UPDATE USER ENDPOINT ===");
        System.out.println("UserId: " + userId);
        System.out.println("Resource: " + resource);
        System.out.println("ProfilePicture in resource: " + resource.profilePicture());
        
        var updateUserCommand = UpdateUserCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var user = userCommandService.handle(updateUserCommand);
        
        if (user.isEmpty()) {
            System.out.println("ERROR: User not found or update failed");
            return ResponseEntity.notFound().build();
        }
        
        System.out.println("SUCCESS: User updated. New profilePicture: " + user.get().getProfilePicture());
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }
    
    @PutMapping("/upgrade-role")
    @Operation(summary = "Upgrade user role", description = "Upgrade user role from HOST to BOTH or GUEST to BOTH when user wants to access features from other app")
    public ResponseEntity<UserResource> upgradeUserRole(@RequestBody UpgradeUserRoleResource resource) {
        var upgradeCommand = UpgradeUserRoleCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(upgradeCommand);
        
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }
    
    @PostMapping("/check-or-upgrade")
    @Operation(summary = "Check user existence or upgrade role", description = "Check if user exists by email, if exists and has different role, upgrade to BOTH")
    public ResponseEntity<?> checkOrUpgradeUser(@RequestBody UpgradeUserRoleResource resource) {

        var existingUserQuery = new GetUserByEmailQuery(resource.email());
        var existingUser = userQueryService.handle(existingUserQuery);
        
        if (existingUser.isPresent()) {
            var user = existingUser.get();
            

            if (user.getRole() == resource.requestedRole() || user.getRole().name().equals("BOTH")) {
                var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user);
                return ResponseEntity.ok(userResource);
            }
            

            if (user.canUpgradeToRole(resource.requestedRole())) {
                var upgradeCommand = UpgradeUserRoleCommandFromResourceAssembler.toCommandFromResource(resource);
                var upgradedUser = userCommandService.handle(upgradeCommand);
                
                if (upgradedUser.isPresent()) {
                    var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(upgradedUser.get());
                    return ResponseEntity.ok(userResource);
                }
            }
        }
        

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("User not found", "Please register with this email first"));
    }
    
    @DeleteMapping("/remove-role")
    @Operation(summary = "Remove user role with cleanup", description = "Remove specific role from user and cleanup all related data")
    public ResponseEntity<UserResource> removeUserRole(@RequestBody RemoveUserRoleResource resource) {
        var removeCommand = RemoveUserRoleCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(removeCommand);
        
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }
    
    @DeleteMapping("/remove-host-role/{userId}")
    @Operation(summary = "Remove HOST role", description = "Remove HOST role from user - cancels all parkings and related bookings")
    public ResponseEntity<UserResource> removeHostRole(@PathVariable Long userId) {
        var removeCommand = new RemoveUserRoleCommand(userId, UserRole.HOST);
        var user = userCommandService.handle(removeCommand);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }
    
    @DeleteMapping("/remove-guest-role/{userId}")
    @Operation(summary = "Remove GUEST role", description = "Remove GUEST role from user - cancels all bookings")
    public ResponseEntity<UserResource> removeGuestRole(@PathVariable Long userId) {
        var removeCommand = new RemoveUserRoleCommand(userId, UserRole.GUEST);
        var user = userCommandService.handle(removeCommand);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }
    
    @PutMapping("/{userId}/profile-picture")
    @Operation(summary = "Update user profile picture", description = "Update user profile picture URL")
    public ResponseEntity<UserResource> updateUserProfilePicture(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestBody Map<String, String> request) {
        
        System.out.println("=== UPDATE PROFILE PICTURE ENDPOINT ===");
        System.out.println("UserId: " + userId);
        System.out.println("Request body: " + request);
        
        String profilePictureUrl = request.get("profilePictureUrl");
        System.out.println("ProfilePictureUrl: " + profilePictureUrl);
        
        if (profilePictureUrl == null || profilePictureUrl.trim().isEmpty()) {
            System.out.println("ERROR: profilePictureUrl is null or empty");
            return ResponseEntity.badRequest().build();
        }
        
        var updateCommand = new UpdateUserProfilePictureCommand(userId, profilePictureUrl);
        var user = userCommandService.handle(updateCommand);
        
        if (user.isEmpty()) {
            System.out.println("ERROR: User not found or update failed");
            return ResponseEntity.notFound().build();
        }
        
        System.out.println("SUCCESS: User profile picture updated");
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }
    

    public record ErrorResponse(String error, String message) {}
}
