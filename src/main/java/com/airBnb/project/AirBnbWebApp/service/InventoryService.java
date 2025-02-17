package com.airBnb.project.AirBnbWebApp.service;
import com.airBnb.project.AirBnbWebApp.dto.*;
import com.airBnb.project.AirBnbWebApp.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InventoryService {



    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);



    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
