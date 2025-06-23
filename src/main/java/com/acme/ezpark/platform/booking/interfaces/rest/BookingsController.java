package com.acme.ezpark.platform.booking.interfaces.rest;

import com.acme.ezpark.platform.booking.domain.model.aggregates.Booking;
import com.acme.ezpark.platform.booking.domain.model.commands.*;
import com.acme.ezpark.platform.booking.domain.model.queries.*;
import com.acme.ezpark.platform.booking.domain.model.valueobjects.BookingStatus;
import com.acme.ezpark.platform.booking.domain.services.BookingCommandService;
import com.acme.ezpark.platform.booking.domain.services.BookingQueryService;
import com.acme.ezpark.platform.booking.interfaces.rest.resources.*;
import com.acme.ezpark.platform.booking.interfaces.rest.transform.*;
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
@Tag(name = "Bookings", description = "Booking Management Endpoints")
public class BookingsController {

    private final BookingCommandService bookingCommandService;
    private final BookingQueryService bookingQueryService;

    public BookingsController(BookingCommandService bookingCommandService, BookingQueryService bookingQueryService) {
        this.bookingCommandService = bookingCommandService;
        this.bookingQueryService = bookingQueryService;
    }

    @PostMapping("/users/{userId}/bookings")
    @Operation(summary = "Create a new booking", description = "Create a new booking for a user")
    public ResponseEntity<BookingResource> createBooking(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestBody CreateBookingResource resource) {
        var createBookingCommand = CreateBookingCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var booking = bookingCommandService.handle(createBookingCommand);
        
        if (booking.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var bookingResource = BookingResourceFromEntityAssembler.toResourceFromEntity(booking.get());
        return new ResponseEntity<>(bookingResource, HttpStatus.CREATED);
    }

    @GetMapping("/bookings/{bookingId}")
    @Operation(summary = "Get booking by ID", description = "Get booking information by booking ID")
    public ResponseEntity<BookingResource> getBookingById(
            @Parameter(description = "Booking ID") @PathVariable Long bookingId) {
        var getBookingByIdQuery = new GetBookingByIdQuery(bookingId);
        var booking = bookingQueryService.handle(getBookingByIdQuery);
        
        if (booking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var bookingResource = BookingResourceFromEntityAssembler.toResourceFromEntity(booking.get());
        return ResponseEntity.ok(bookingResource);
    }

    @GetMapping("/users/{userId}/bookings")
    @Operation(summary = "Get user bookings", description = "Get all bookings for a specific user")
    public ResponseEntity<List<BookingResource>> getUserBookings(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        var getUserBookingsQuery = new GetBookingsByUserIdQuery(userId);
        var bookings = bookingQueryService.handle(getUserBookingsQuery);
        
        var bookingResources = bookings.stream()
            .map(BookingResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(bookingResources);
    }

    @GetMapping("/users/{userId}/bookings/status/{status}")
    @Operation(summary = "Get user bookings by status", description = "Get user bookings filtered by status")
    public ResponseEntity<List<BookingResource>> getUserBookingsByStatus(        @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Booking Status") @PathVariable BookingStatus status) {
        var getUserBookingsByStatusQuery = new GetBookingsByUserIdAndStatusQuery(userId, status);
        var bookings = bookingQueryService.handle(getUserBookingsByStatusQuery);
        
        var bookingResources = bookings.stream()
            .map(BookingResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(bookingResources);
    }

    @PutMapping("/bookings/{bookingId}/confirm")
    @Operation(summary = "Confirm booking", description = "Confirm a pending booking")
    public ResponseEntity<BookingResource> confirmBooking(
            @Parameter(description = "Booking ID") @PathVariable Long bookingId) {
        var confirmBookingCommand = new ConfirmBookingCommand(bookingId);
        var booking = bookingCommandService.handle(confirmBookingCommand);
        
        if (booking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var bookingResource = BookingResourceFromEntityAssembler.toResourceFromEntity(booking.get());
        return ResponseEntity.ok(bookingResource);
    }

    @PutMapping("/bookings/{bookingId}/start")
    @Operation(summary = "Start booking", description = "Start a confirmed booking")
    public ResponseEntity<BookingResource> startBooking(
            @Parameter(description = "Booking ID") @PathVariable Long bookingId) {
        var startBookingCommand = new StartBookingCommand(bookingId);
        var booking = bookingCommandService.handle(startBookingCommand);
        
        if (booking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var bookingResource = BookingResourceFromEntityAssembler.toResourceFromEntity(booking.get());
        return ResponseEntity.ok(bookingResource);
    }    @PutMapping("/bookings/{bookingId}/complete")
    @Operation(summary = "Complete booking", description = "Complete an in-progress booking")
    public ResponseEntity<BookingResource> completeBooking(
            @Parameter(description = "Booking ID") @PathVariable Long bookingId) {
        var completeBookingCommand = new CompleteBookingCommand(bookingId);
        var booking = bookingCommandService.handle(completeBookingCommand);
        
        if (booking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var bookingResource = BookingResourceFromEntityAssembler.toResourceFromEntity(booking.get());
        return ResponseEntity.ok(bookingResource);
    }

    @PutMapping("/bookings/{bookingId}/cancel")
    @Operation(summary = "Cancel booking", description = "Cancel a booking")
    public ResponseEntity<BookingResource> cancelBooking(
            @Parameter(description = "Booking ID") @PathVariable Long bookingId,
            @RequestBody CancelBookingResource resource) {
        var cancelBookingCommand = new CancelBookingCommand(bookingId, resource.cancellationReason());
        var booking = bookingCommandService.handle(cancelBookingCommand);
        
        if (booking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var bookingResource = BookingResourceFromEntityAssembler.toResourceFromEntity(booking.get());
        return ResponseEntity.ok(bookingResource);
    }
    
    @GetMapping("/bookings/{bookingId}/cancel-info")
    @Operation(summary = "Get booking cancellation info", description = "Get information about booking cancellation constraints")
    public ResponseEntity<BookingCancelInfoResource> getBookingCancelInfo(
            @Parameter(description = "Booking ID") @PathVariable Long bookingId) {
        var getBookingByIdQuery = new GetBookingByIdQuery(bookingId);
        var booking = bookingQueryService.handle(getBookingByIdQuery);
        
        if (booking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
          var bookingEntity = booking.get();
        var cancelDeadline = bookingEntity.getStartTime().minus(15, java.time.temporal.ChronoUnit.MINUTES);
        var cancelInfo = new BookingCancelInfoResource(
            bookingEntity.canBeCancelled(),
            bookingEntity.getMinutesUntilCancelDeadline(),
            cancelDeadline,
            "Must cancel at least 15 minutes before start time"
        );
        
        return ResponseEntity.ok(cancelInfo);
    }
    
    @GetMapping("/bookings/active")
    @Operation(summary = "Get active bookings", description = "Get all currently active bookings (for monitoring)")
    public ResponseEntity<List<BookingResource>> getActiveBookings() {
        var activeBookings = bookingQueryService.getAllBookings().stream()
            .filter(booking -> booking.getStatus() == BookingStatus.ACTIVE)
            .map(BookingResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(activeBookings);
    }

    @GetMapping("/parkings/{parkingId}/bookings")
    @Operation(summary = "Get parking bookings", description = "Get all bookings for a specific parking (for hosts)")
    public ResponseEntity<List<BookingResource>> getParkingBookings(
            @Parameter(description = "Parking ID") @PathVariable Long parkingId) {
        var getParkingBookingsQuery = new GetBookingsByParkingIdQuery(parkingId);
        var bookings = bookingQueryService.handle(getParkingBookingsQuery);
        
        var bookingResources = bookings.stream()
            .map(BookingResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(bookingResources);
    }

    @GetMapping("/parkings/{parkingId}/bookings/status/{status}")
    @Operation(summary = "Get parking bookings by status", description = "Get parking bookings filtered by status (for hosts)")
    public ResponseEntity<List<BookingResource>> getParkingBookingsByStatus(
            @Parameter(description = "Parking ID") @PathVariable Long parkingId,
            @Parameter(description = "Booking Status") @PathVariable BookingStatus status) {
        var getParkingBookingsByStatusQuery = new GetBookingsByParkingIdAndStatusQuery(parkingId, status);
        var bookings = bookingQueryService.handle(getParkingBookingsByStatusQuery);
        
        var bookingResources = bookings.stream()
            .map(BookingResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(bookingResources);
    }
}
