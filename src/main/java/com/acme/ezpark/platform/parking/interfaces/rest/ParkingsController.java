package com.acme.ezpark.platform.parking.interfaces.rest;

import com.acme.ezpark.platform.parking.domain.model.commands.DeleteParkingCommand;
import com.acme.ezpark.platform.parking.domain.model.queries.GetAllParkingsQuery;
import com.acme.ezpark.platform.parking.domain.model.queries.GetParkingByIdQuery;
import com.acme.ezpark.platform.parking.domain.model.queries.GetParkingsByLocationQuery;
import com.acme.ezpark.platform.parking.domain.model.queries.GetParkingsByOwnerIdQuery;
import com.acme.ezpark.platform.parking.domain.services.ParkingCommandService;
import com.acme.ezpark.platform.parking.domain.services.ParkingQueryService;
import com.acme.ezpark.platform.parking.interfaces.rest.resources.CreateParkingResource;
import com.acme.ezpark.platform.parking.interfaces.rest.resources.ParkingResource;
import com.acme.ezpark.platform.parking.interfaces.rest.resources.UpdateParkingResource;
import com.acme.ezpark.platform.parking.interfaces.rest.transform.CreateParkingCommandFromResourceAssembler;
import com.acme.ezpark.platform.parking.interfaces.rest.transform.ParkingResourceFromEntityAssembler;
import com.acme.ezpark.platform.parking.interfaces.rest.transform.UpdateParkingCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Parkings", description = "Parking Management Endpoints")
public class ParkingsController {

    private final ParkingCommandService parkingCommandService;
    private final ParkingQueryService parkingQueryService;

    public ParkingsController(ParkingCommandService parkingCommandService, ParkingQueryService parkingQueryService) {
        this.parkingCommandService = parkingCommandService;
        this.parkingQueryService = parkingQueryService;
    }

    @PostMapping("/users/{ownerId}/parkings")
    @Operation(summary = "Create a new parking", description = "Create a new parking space for a user")
    public ResponseEntity<ParkingResource> createParking(
            @Parameter(description = "Owner ID") @PathVariable Long ownerId,
            @RequestBody CreateParkingResource resource) {
        var createParkingCommand = CreateParkingCommandFromResourceAssembler.toCommandFromResource(ownerId, resource);
        var parking = parkingCommandService.handle(createParkingCommand);
        
        if (parking.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var parkingResource = ParkingResourceFromEntityAssembler.toResourceFromEntity(parking.get());
        return new ResponseEntity<>(parkingResource, HttpStatus.CREATED);
    }

    @GetMapping("/parkings")
    @Operation(summary = "Get all parkings", description = "Get all parkings (for debugging purposes)")
    public ResponseEntity<List<ParkingResource>> getAllParkings() {
        var getAllParkingsQuery = new GetAllParkingsQuery();
        var parkings = parkingQueryService.handle(getAllParkingsQuery);
        
        var parkingResources = parkings.stream()
            .map(ParkingResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(parkingResources);
    }

    @GetMapping("/parkings/{parkingId}")
    @Operation(summary = "Get parking by ID", description = "Get parking information by parking ID")
    public ResponseEntity<ParkingResource> getParkingById(
            @Parameter(description = "Parking ID") @PathVariable Long parkingId) {
        var getParkingByIdQuery = new GetParkingByIdQuery(parkingId);
        var parking = parkingQueryService.handle(getParkingByIdQuery);
        
        if (parking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var parkingResource = ParkingResourceFromEntityAssembler.toResourceFromEntity(parking.get());
        return ResponseEntity.ok(parkingResource);
    }

    @GetMapping("/users/{ownerId}/parkings")
    @Operation(summary = "Get owner parkings", description = "Get all parkings for a specific owner")
    public ResponseEntity<List<ParkingResource>> getOwnerParkings(
            @Parameter(description = "Owner ID") @PathVariable Long ownerId) {
        var getOwnerParkingsQuery = new GetParkingsByOwnerIdQuery(ownerId);
        var parkings = parkingQueryService.handle(getOwnerParkingsQuery);
        
        var parkingResources = parkings.stream()
            .map(ParkingResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(parkingResources);
    }

    @GetMapping("/parkings/search")
    @Operation(summary = "Search parkings by location", description = "Find parkings near a specific location")
    public ResponseEntity<List<ParkingResource>> searchParkingsByLocation(
            @Parameter(description = "Latitude") @RequestParam BigDecimal latitude,
            @Parameter(description = "Longitude") @RequestParam BigDecimal longitude,
            @Parameter(description = "Radius in kilometers") @RequestParam(defaultValue = "5.0") BigDecimal radiusKm) {
        var searchQuery = new GetParkingsByLocationQuery(latitude, longitude, radiusKm);
        var parkings = parkingQueryService.handle(searchQuery);
        
        var parkingResources = parkings.stream()
            .map(ParkingResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(parkingResources);
    }

    @PutMapping("/parkings/{parkingId}")
    @Operation(summary = "Update parking", description = "Update parking information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking updated"),
            @ApiResponse(responseCode = "404", description = "Parking not found")
    })
    public ResponseEntity<ParkingResource> updateParking(
            @Parameter(description = "Parking ID") @PathVariable Long parkingId,
            @RequestBody UpdateParkingResource resource) {
        var updateParkingCommand = UpdateParkingCommandFromResourceAssembler.toCommandFromResource(parkingId, resource);
        var parking = parkingCommandService.handle(updateParkingCommand);
        
        if (parking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var parkingResource = ParkingResourceFromEntityAssembler.toResourceFromEntity(parking.get());
        return ResponseEntity.ok(parkingResource);
    }    @DeleteMapping("/parkings/{parkingId}")
    @Operation(summary = "Delete parking", description = "Delete a parking permanently")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parking deleted"),
            @ApiResponse(responseCode = "404", description = "Parking not found")
    })
    public ResponseEntity<Void> deleteParking(
            @Parameter(description = "Parking ID") @PathVariable Long parkingId) {
        var deleteParkingCommand = new DeleteParkingCommand(parkingId);
        var result = parkingCommandService.handle(deleteParkingCommand);
        
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.noContent().build();
    }
}
