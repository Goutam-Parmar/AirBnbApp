package com.airBnb.project.AirBnbWebApp.dto;

import com.airBnb.project.AirBnbWebApp.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateDto {
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}
