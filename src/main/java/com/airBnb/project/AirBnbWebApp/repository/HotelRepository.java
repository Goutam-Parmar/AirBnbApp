package com.airBnb.project.AirBnbWebApp.repository;

import com.airBnb.project.AirBnbWebApp.entity.Hotel;
import com.airBnb.project.AirBnbWebApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel , Long> {
    List<Hotel> findByOwner(User user);
}
