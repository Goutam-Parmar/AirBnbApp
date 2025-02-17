package com.airBnb.project.AirBnbWebApp.service;


import com.airBnb.project.AirBnbWebApp.dto.RoomDto;
//import com.airBnb.project.AirBnbWebApp.entity.Room;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId ,RoomDto roomDto);

    List<RoomDto> getAllRoomsInHotel(long hotelId);

    RoomDto getRoomById(Long roomId);

    void deleteRoomById(Long roomId);

    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);
}
