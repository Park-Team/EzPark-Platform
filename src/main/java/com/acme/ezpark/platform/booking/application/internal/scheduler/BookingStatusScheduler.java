package com.acme.ezpark.platform.booking.application.internal.scheduler;

import com.acme.ezpark.platform.booking.domain.model.aggregates.Booking;
import com.acme.ezpark.platform.booking.domain.model.valueobjects.BookingStatus;
import com.acme.ezpark.platform.booking.infrastructure.persistence.jpa.repositories.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class BookingStatusScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BookingStatusScheduler.class);
    private final BookingRepository bookingRepository;

    public BookingStatusScheduler(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    @Scheduled(fixedRate = 60000) // Every minute
    @Transactional
    public void updateBookingStatuses() {
        try {
            logger.debug("Starting automatic booking status update at {}", Instant.now());

            Instant now = Instant.now();
            int updatedCount = 0;

            // 1. PENDING → CONFIRMED (15 minutes before start time)
            Instant confirmDeadline = now.plus(15, ChronoUnit.MINUTES);
            List<Booking> pendingBookings = bookingRepository.findByStatusAndStartTimeBefore(
                BookingStatus.PENDING, confirmDeadline);
            
            for (Booking booking : pendingBookings) {
                if (booking.shouldBeConfirmed()) {
                    BookingStatus originalStatus = booking.getStatus();
                    booking.confirmBooking();
                    bookingRepository.save(booking);
                    updatedCount++;
                    logger.info("Booking {} transitioned from {} to {} at {}", 
                        booking.getId(), originalStatus, booking.getStatus(), now);
                }
            }

            // 2. CONFIRMED → ACTIVE (at start time)
            List<Booking> confirmedBookings = bookingRepository.findByStatus(BookingStatus.CONFIRMED);
            for (Booking booking : confirmedBookings) {
                if (booking.shouldBeActive()) {
                    BookingStatus originalStatus = booking.getStatus();
                    booking.startBooking();
                    bookingRepository.save(booking);
                    updatedCount++;
                    logger.info("Booking {} transitioned from {} to {} at {}", 
                        booking.getId(), originalStatus, booking.getStatus(), now);
                }
            }            // 3. ACTIVE → COMPLETED (at end time)
            List<Booking> activeBookings = bookingRepository.findByStatus(BookingStatus.ACTIVE);
            for (Booking booking : activeBookings) {
                if (booking.shouldBeCompleted()) {
                    BookingStatus originalStatus = booking.getStatus();
                    booking.completeBooking();
                    bookingRepository.save(booking);
                    updatedCount++;
                    logger.info("Booking {} transitioned from {} to {} at {}", 
                        booking.getId(), originalStatus, booking.getStatus(), now);
                }
            }

            logger.debug("Booking status update completed. Updated {} bookings at {}", updatedCount, now);
            
        } catch (Exception e) {
            logger.error("Error during automatic booking status update", e);
        }
    }
}
