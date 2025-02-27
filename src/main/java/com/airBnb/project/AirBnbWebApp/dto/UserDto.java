package com.airBnb.project.AirBnbWebApp.dto;

import com.airBnb.project.AirBnbWebApp.entity.enums.Gender;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private Gender dateOfBirth;
}
