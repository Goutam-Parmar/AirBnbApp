package com.airBnb.project.AirBnbWebApp.service;


import com.airBnb.project.AirBnbWebApp.dto.*;
import com.airBnb.project.AirBnbWebApp.entity.Hotel;
import com.airBnb.project.AirBnbWebApp.entity.Inventory;
import com.airBnb.project.AirBnbWebApp.entity.Room;
import com.airBnb.project.AirBnbWebApp.entity.User;
import com.airBnb.project.AirBnbWebApp.exception.ResourceNotFoundException;
import com.airBnb.project.AirBnbWebApp.repository.HotelMinPriceRepository;
import com.airBnb.project.AirBnbWebApp.repository.InventoryRepository;
import com.airBnb.project.AirBnbWebApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.airBnb.project.AirBnbWebApp.util.Apputils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImp implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final RoomRepository roomRepository;
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

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId) {
        log.info("Getting All inventory by room with id:{}",roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->new ResourceNotFoundException("Room not found with id :"+ roomId));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("you are not the owner of room with ID:"+roomId);

        return  inventoryRepository.findByRoomOrderByDate(room).stream()
                .map((element) -> modelMapper.map(element,
                        InventoryDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("updating All inventory by room with id:{} between date range :{} -{}",roomId,
                updateInventoryRequestDto.getStartDate(),updateInventoryRequestDto.getEndDate());

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->new ResourceNotFoundException("Room not found with id :"+ roomId));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("you are not the owner of room with ID:"+roomId);

        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId,updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate());


         inventoryRepository.updateInventory(roomId,updateInventoryRequestDto.getStartDate(),
                 updateInventoryRequestDto.getEndDate(),
                 updateInventoryRequestDto.getClosed(),
                 updateInventoryRequestDto.getSurgeFactor());



    }

}
