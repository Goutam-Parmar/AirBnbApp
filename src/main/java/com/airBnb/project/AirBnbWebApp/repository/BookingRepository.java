package com.airBnb.project.AirBnbWebApp.repository;

import com.airBnb.project.AirBnbWebApp.dto.BookingDto;
import com.airBnb.project.AirBnbWebApp.entity.Booking;
import com.airBnb.project.AirBnbWebApp.entity.Hotel;
import com.airBnb.project.AirBnbWebApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking  ,Long> {
    Optional<Booking> findByPaymentSessionId(String sessionId);

    List<Booking> findByHotel(Hotel hotel);

    List<Booking> findByHotelAndCreatedAtBetween(Hotel hotel , LocalDateTime startDateTime , LocalDateTime endDateTime);

    List<Booking> findByUser(User user);
}
