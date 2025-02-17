package com.airBnb.project.AirBnbWebApp.service;

import com.airBnb.project.AirBnbWebApp.dto.ProfileUpdateDto;
import com.airBnb.project.AirBnbWebApp.dto.UserDto;
import com.airBnb.project.AirBnbWebApp.entity.User;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateDto profileUpdateDto);

    UserDto getMyProfile();
}
