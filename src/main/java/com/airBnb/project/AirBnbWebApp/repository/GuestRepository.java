package com.airBnb.project.AirBnbWebApp.repository;

import com.airBnb.project.AirBnbWebApp.entity.Guest;
import com.airBnb.project.AirBnbWebApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest , Long>{


    List<Guest> findByUser(User user);
}
