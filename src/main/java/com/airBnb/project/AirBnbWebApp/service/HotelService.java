package com.airBnb.project.AirBnbWebApp.service;

import com.airBnb.project.AirBnbWebApp.dto.HotelDto;
import com.airBnb.project.AirBnbWebApp.dto.HotelInfoDto;
import com.airBnb.project.AirBnbWebApp.entity.Hotel;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id , HotelDto hotelDto);



    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId);
}
