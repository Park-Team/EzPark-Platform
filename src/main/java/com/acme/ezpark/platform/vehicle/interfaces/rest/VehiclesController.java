package com.acme.ezpark.platform.vehicle.interfaces.rest;

import com.acme.ezpark.platform.vehicle.domain.model.commands.DeleteVehicleCommand;
import com.acme.ezpark.platform.vehicle.domain.model.queries.GetVehicleByIdQuery;
import com.acme.ezpark.platform.vehicle.domain.model.queries.GetVehiclesByUserIdQuery;
import com.acme.ezpark.platform.vehicle.domain.services.VehicleCommandService;
import com.acme.ezpark.platform.vehicle.domain.services.VehicleQueryService;
import com.acme.ezpark.platform.vehicle.interfaces.rest.resources.*;
import com.acme.ezpark.platform.vehicle.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vehicles", description = "Vehicle Management Endpoints")
public class VehiclesController {

    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;

    public VehiclesController(VehicleCommandService vehicleCommandService, VehicleQueryService vehicleQueryService) {
        this.vehicleCommandService = vehicleCommandService;
        this.vehicleQueryService = vehicleQueryService;
    }

    @PostMapping("/users/{userId}/vehicles")
    @Operation(summary = "Create a new vehicle", description = "Create a new vehicle for a user")
    public ResponseEntity<VehicleResource> createVehicle(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestBody CreateVehicleResource resource) {
        var createVehicleCommand = CreateVehicleCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var vehicle = vehicleCommandService.handle(createVehicleCommand);
        
        if (vehicle.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var vehicleResource = VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle.get());
        return new ResponseEntity<>(vehicleResource, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/vehicles")
    @Operation(summary = "Get user vehicles", description = "Get all vehicles for a specific user")
    public ResponseEntity<List<VehicleResource>> getUserVehicles(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        var getUserVehiclesQuery = new GetVehiclesByUserIdQuery(userId);
        var vehicles = vehicleQueryService.handle(getUserVehiclesQuery);
        
        var vehicleResources = vehicles.stream()
            .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(vehicleResources);
    }

    @GetMapping("/vehicles/{vehicleId}")
    @Operation(summary = "Get vehicle by ID", description = "Get vehicle information by vehicle ID")
    public ResponseEntity<VehicleResource> getVehicleById(
            @Parameter(description = "Vehicle ID") @PathVariable Long vehicleId) {
        var getVehicleByIdQuery = new GetVehicleByIdQuery(vehicleId);
        var vehicle = vehicleQueryService.handle(getVehicleByIdQuery);
        
        if (vehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var vehicleResource = VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle.get());
        return ResponseEntity.ok(vehicleResource);
    }

    @PutMapping("/vehicles/{vehicleId}")
    @Operation(summary = "Update vehicle", description = "Update vehicle information")
    public ResponseEntity<VehicleResource> updateVehicle(
            @Parameter(description = "Vehicle ID") @PathVariable Long vehicleId,
            @RequestBody UpdateVehicleResource resource) {
        var updateVehicleCommand = UpdateVehicleCommandFromResourceAssembler.toCommandFromResource(vehicleId, resource);
        var vehicle = vehicleCommandService.handle(updateVehicleCommand);
        
        if (vehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var vehicleResource = VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle.get());
        return ResponseEntity.ok(vehicleResource);
    }

    @DeleteMapping("/vehicles/{vehicleId}")
    @Operation(summary = "Delete vehicle", description = "Delete a vehicle (soft delete)")
    public ResponseEntity<Void> deleteVehicle(
            @Parameter(description = "Vehicle ID") @PathVariable Long vehicleId) {
        var deleteVehicleCommand = new DeleteVehicleCommand(vehicleId);
        var result = vehicleCommandService.handle(deleteVehicleCommand);
        
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.noContent().build();
    }
}
