package com.airBnb.project.AirBnbWebApp.service;


import com.airBnb.project.AirBnbWebApp.dto.HotelDto;
import com.airBnb.project.AirBnbWebApp.dto.HotelInfoDto;
import com.airBnb.project.AirBnbWebApp.dto.RoomDto;
import com.airBnb.project.AirBnbWebApp.entity.Hotel;
import com.airBnb.project.AirBnbWebApp.entity.Room;
import com.airBnb.project.AirBnbWebApp.entity.User;
import com.airBnb.project.AirBnbWebApp.exception.ResourceNotFoundException;
import com.airBnb.project.AirBnbWebApp.exception.UnAuthorisedException;
import com.airBnb.project.AirBnbWebApp.repository.HotelRepository;
import com.airBnb.project.AirBnbWebApp.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImp implements HotelService{



    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;
    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Created a new hotel with name: {}",hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID: {}",hotelDto.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting the hotel with ID: {}",id);
       Hotel hotel =  hotelRepository
               .findById(id)
               .orElseThrow(() ->new ResourceNotFoundException("Hotel not found with ID "+id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
          throw new UnAuthorisedException("This user does not own this hotel with id:"+id);
        }

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id , HotelDto hotelDto){

        log.info("Updating  the hotel with ID: {}",id);
        Hotel hotel =  hotelRepository
                .findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel not found with ID "+id));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id:"+id);
        }

        modelMapper.map(hotelDto , hotel);
          hotel.setId(id);
          hotel = hotelRepository.save(hotel);
          return modelMapper.map(hotel , HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id){
        Hotel hotel =  hotelRepository
                .findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel not found with ID "+id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id:"+id);
        }




        //  delete the future inventories
        for (Room room : hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());

        }
        hotelRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId){
        log.info("Activating  the hotel with ID: {}",hotelId);
        Hotel hotel =  hotelRepository
                .findById(hotelId)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel not found with ID "+hotelId));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id:"+hotelId);
        }

        hotel.setActive(true);
        //  create the inventory for all the room
        //assuming only do it once
       for (Room room : hotel.getRooms()){
           inventoryService.initializeRoomForAYear(room);
       }
    }
//public method
    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel =  hotelRepository
                .findById(hotelId)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel not found with ID "+hotelId));

        List<RoomDto> rooms = hotel.getRooms()
                .stream()
                .map((element) ->modelMapper.map(element, RoomDto.class))
                .toList();
        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class),rooms);
    }
}
