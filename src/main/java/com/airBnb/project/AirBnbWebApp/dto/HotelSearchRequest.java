package com.airBnb.project.AirBnbWebApp.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
public class HotelSearchRequest {
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomsCount;
    private Integer page =0;
    private Integer size =10;
}
