package com.airBnb.project.AirBnbWebApp.service;

//import com.airBnb.project.AirBnbWebApp.dto.HotelDto;
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

import static com.airBnb.project.AirBnbWebApp.util.Apputils.getCurrentUser;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImp implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper   modelMapper;

    @Override
    public RoomDto createNewRoom(Long hotelId , RoomDto roomDto) {
        log.info("Creating  new room in hotel with ID:{}",hotelId);

        Hotel hotel =  hotelRepository
                .findById(hotelId)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel not found with ID "+hotelId));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id:"+hotelId);
        }


        Room room  = modelMapper.map(roomDto ,Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);


        //create the inventory as soon the room is created and hotel is active

        if(hotel.getActive()){
            inventoryService.initializeRoomForAYear(room);

        }
        return modelMapper.map(room , RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(long hotelId) {
        log.info("Getting all room in hotel with ID:{}",hotelId);

        Hotel hotel =  hotelRepository
                .findById(hotelId)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel not found with ID "+hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id:"+hotelId);
        }


        return hotel.getRooms()
                .stream()
                .map((element)-> modelMapper.map(element , RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting the room with room  ID:{}",roomId);

        Room  room =  roomRepository
                .findById(roomId)
                .orElseThrow(() ->new ResourceNotFoundException("Room not found with ID "+roomId));
        return  modelMapper.map(room , RoomDto.class);
    }
    @Transactional
    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Delete the room with ID:{}",roomId);
        Room  room =  roomRepository
                .findById(roomId)
                .orElseThrow(() ->new ResourceNotFoundException("Room not found with ID "+roomId));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorisedException("This user does not own this room with id:"+roomId);
        }


        // delete all future inventory for this room
        inventoryService.deleteAllInventories(room);
     roomRepository.deleteById(roomId);
    }

    @Override
    @Transactional
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {
        log.info("Updating  the hotel with ID: {}",hotelId);
        Hotel hotel =  hotelRepository
                .findById(hotelId)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel not found with ID "+hotelId));

        User user = getCurrentUser();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id:"+hotelId);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with ID:"+roomId));

        modelMapper.map(roomDto,room);
        room.setId(roomId);

        //TODO: if price or inventory is updated , then update the inventory for this room
        room = roomRepository.save(room);


        return modelMapper.map(room , RoomDto.class);
    }
}
