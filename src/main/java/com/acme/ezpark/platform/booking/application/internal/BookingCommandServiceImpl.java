package com.acme.ezpark.platform.booking.application.internal;

import com.acme.ezpark.platform.booking.domain.model.aggregates.Booking;
import com.acme.ezpark.platform.booking.domain.model.commands.*;
import com.acme.ezpark.platform.booking.domain.services.BookingCommandService;
import com.acme.ezpark.platform.booking.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.acme.ezpark.platform.parking.infrastructure.persistence.jpa.repositories.ParkingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingCommandServiceImpl implements BookingCommandService {

    private final BookingRepository bookingRepository;
    private final ParkingRepository parkingRepository;

    public BookingCommandServiceImpl(BookingRepository bookingRepository, ParkingRepository parkingRepository) {
        this.bookingRepository = bookingRepository;
        this.parkingRepository = parkingRepository;
    }    @Override
    public Optional<Booking> handle(CreateBookingCommand command) {
        // First, get the parking to obtain the price per hour
        var parkingOpt = parkingRepository.findById(command.parkingId());
        if (parkingOpt.isEmpty()) {
            throw new RuntimeException("Parking not found with id: " + command.parkingId());
        }
        
        var parking = parkingOpt.get();
        var booking = new Booking(
            command.userId(),
            command.parkingId(),
            command.vehicleId(),
            command.startTime(),
            command.endTime(),
            parking.getPricePerHour(),
            command.notes()
        );

        try {
            bookingRepository.save(booking);
            return Optional.of(booking);
        } catch (Exception e) {
            return Optional.empty();
        }
    }@Override
    public Optional<Booking> handle(CancelBookingCommand command) {
        return bookingRepository.findById(command.bookingId())
            .map(booking -> {
                // Validate cancellation time constraints
                if (!booking.canBeCancelled()) {
                    throw new RuntimeException("Cannot cancel booking - cancellation deadline has passed (must cancel at least 15 minutes before start time)");
                }
                
                booking.cancelBooking(command.cancellationReason());
                bookingRepository.save(booking);
                return booking;
            });
    }

    @Override
    public Optional<Booking> handle(ConfirmBookingCommand command) {
        return bookingRepository.findById(command.bookingId())
            .map(booking -> {
                booking.confirmBooking();
                bookingRepository.save(booking);
                return booking;
            });
    }

    @Override
    public Optional<Booking> handle(StartBookingCommand command) {
        return bookingRepository.findById(command.bookingId())
            .map(booking -> {
                booking.startBooking();
                bookingRepository.save(booking);
                return booking;
            });
    }    @Override
    public Optional<Booking> handle(CompleteBookingCommand command) {
        return bookingRepository.findById(command.bookingId())
            .map(booking -> {
                booking.completeBooking();
                bookingRepository.save(booking);
                return booking;
            });
    }
}
