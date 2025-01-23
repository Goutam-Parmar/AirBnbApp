package com.airBnb.project.AirBnbWebApp.service;
import com.airBnb.project.AirBnbWebApp.dto.HotelDto;
import com.airBnb.project.AirBnbWebApp.dto.HotelPriceDto;
import com.airBnb.project.AirBnbWebApp.dto.HotelSearchRequest;
import com.airBnb.project.AirBnbWebApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {



    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);



    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
