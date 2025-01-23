package com.airBnb.project.AirBnbWebApp.repository;

import com.airBnb.project.AirBnbWebApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
