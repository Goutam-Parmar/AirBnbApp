package com.airBnb.project.AirBnbWebApp.dto;

import lombok.Data;
import org.springframework.security.core.parameters.P;

@Data
public class LoginDto {
    private String email;
    private String password;
}
