package com.airBnb.project.AirBnbWebApp.service;


import com.airBnb.project.AirBnbWebApp.dto.HotelDto;
import com.airBnb.project.AirBnbWebApp.dto.HotelPriceDto;
import com.airBnb.project.AirBnbWebApp.dto.HotelSearchRequest;
import com.airBnb.project.AirBnbWebApp.entity.Hotel;
import com.airBnb.project.AirBnbWebApp.entity.Inventory;
import com.airBnb.project.AirBnbWebApp.entity.Room;
import com.airBnb.project.AirBnbWebApp.repository.HotelMinPriceRepository;
import com.airBnb.project.AirBnbWebApp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImp implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final ModelMapper modelMapper;

    @Override
    public void initializeRoomForAYear(Room room){
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
     for (; !today.isAfter(endDate);today=today.plusDays(1)){
         Inventory inventory =Inventory.builder()
                 .hotel(room.getHotel())
                 .room(room)
                 .bookedCount(0)
                 .reservedCount(0)
                 .city(room.getHotel().getCity())
                 .date(today)
                 .price(room.getBasePrice())
                 .surgeFactor(BigDecimal.ONE)
                 .totalCount(room.getTotalCount())
                 .closed(false)
                 .build();

         inventoryRepository.save(inventory);
     }
    }

    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting the inventories of room with id:{}",room.getId());
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {

        log.info("Searching hotels for {} city , from {} to {}", hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate());
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage() , hotelSearchRequest.getSize());
        long dateCount =
                ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate())+1;

          // business login - 90 days
        Page<HotelPriceDto> hotelPage =
                hotelMinPriceRepository.findHotelsWithAvailableInventory(hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate(),hotelSearchRequest.getRoomsCount(),
                dateCount, pageable);

        return hotelPage;
    }

}
