package com.acme.ezpark.platform.booking.infrastructure.persistence.jpa.repositories;

import com.acme.ezpark.platform.booking.domain.model.aggregates.Booking;
import com.acme.ezpark.platform.booking.domain.model.valueobjects.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
    List<Booking> findByParkingId(Long parkingId);
    List<Booking> findByParkingIdAndStatus(Long parkingId, BookingStatus status);
    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByStatusIn(List<BookingStatus> statuses);
    List<Booking> findByStatusAndStartTimeBefore(BookingStatus status, Instant startTime);
    
    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.startTime <= :currentTime")
    List<Booking> findBookingsToConfirm(@Param("status") BookingStatus status, @Param("currentTime") Instant currentTime);
    
    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.startTime <= :currentTime AND b.endTime > :currentTime")
    List<Booking> findBookingsToActivate(@Param("status") BookingStatus status, @Param("currentTime") Instant currentTime);
    
    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.endTime <= :currentTime")
    List<Booking> findBookingsToComplete(@Param("status") BookingStatus status, @Param("currentTime") Instant currentTime);
}
