package com.acme.ezpark.platform.booking.application.internal;

import com.acme.ezpark.platform.booking.domain.model.aggregates.Booking;
import com.acme.ezpark.platform.booking.domain.model.queries.*;
import com.acme.ezpark.platform.booking.domain.services.BookingQueryService;
import com.acme.ezpark.platform.booking.infrastructure.persistence.jpa.repositories.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingQueryServiceImpl implements BookingQueryService {

    private final BookingRepository bookingRepository;

    public BookingQueryServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Optional<Booking> handle(GetBookingByIdQuery query) {
        return bookingRepository.findById(query.bookingId());
    }

    @Override
    public List<Booking> handle(GetBookingsByUserIdQuery query) {
        return bookingRepository.findByUserId(query.userId());
    }

    @Override
    public List<Booking> handle(GetBookingsByUserIdAndStatusQuery query) {
        return bookingRepository.findByUserIdAndStatus(query.userId(), query.status());
    }    @Override
    public List<Booking> handle(GetBookingsByParkingIdQuery query) {
        return bookingRepository.findByParkingId(query.parkingId());
    }

    @Override
    public List<Booking> handle(GetBookingsByParkingIdAndStatusQuery query) {
        return bookingRepository.findByParkingIdAndStatus(query.parkingId(), query.status());
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
