package com.airBnb.project.AirBnbWebApp.service;

import com.airBnb.project.AirBnbWebApp.dto.BookingDto;
import com.airBnb.project.AirBnbWebApp.dto.BookingRequest;
import com.airBnb.project.AirBnbWebApp.dto.GuestDto;
import com.stripe.model.Event;

import java.util.List;
import java.util.Map;

public interface BookingService {
    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);
}
