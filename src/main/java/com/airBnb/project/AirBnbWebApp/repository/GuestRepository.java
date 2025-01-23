package com.airBnb.project.AirBnbWebApp.repository;

import com.airBnb.project.AirBnbWebApp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest , Long>{
}
