package com.airBnb.project.AirBnbWebApp.service;

import com.airBnb.project.AirBnbWebApp.dto.GuestDto;

import java.util.List;

public interface GuestService {

    List<GuestDto> getAllGuests();

    void updateGuest(Long guestId, GuestDto guestDto);

    void deleteGuest(Long guestId);

    GuestDto addNewGuest(GuestDto guestDto);
}

