package com.airBnb.project.AirBnbWebApp.service;

import com.airBnb.project.AirBnbWebApp.dto.ProfileUpdateDto;
import com.airBnb.project.AirBnbWebApp.dto.UserDto;
import com.airBnb.project.AirBnbWebApp.entity.User;
import com.airBnb.project.AirBnbWebApp.exception.ResourceNotFoundException;
import com.airBnb.project.AirBnbWebApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.airBnb.project.AirBnbWebApp.util.Apputils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id:"+id));
    }

    @Override
    public void updateProfile(ProfileUpdateDto profileUpdateDto) {
        User user = getCurrentUser();

        if(profileUpdateDto.getDateOfBirth() != null) user.setDateOfBirth(profileUpdateDto.getDateOfBirth());
        if(profileUpdateDto.getGender()!= null)user.setGender(profileUpdateDto.getGender());
        if(profileUpdateDto.getName() != null) user.setName(profileUpdateDto.getName());

        userRepository.save(user);
    }

    @Override
    public UserDto getMyProfile() {
        User user = getCurrentUser();
        log.info("Getting the profile for user with id:{}",user.getId());


        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }
}
