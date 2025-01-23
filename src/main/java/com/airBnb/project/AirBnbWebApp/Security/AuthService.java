package com.airBnb.project.AirBnbWebApp.Security;


import com.airBnb.project.AirBnbWebApp.dto.LoginDto;
import com.airBnb.project.AirBnbWebApp.dto.SignUpRequestDto;
import com.airBnb.project.AirBnbWebApp.dto.UserDto;
import com.airBnb.project.AirBnbWebApp.entity.User;
import com.airBnb.project.AirBnbWebApp.entity.enums.Role;
import com.airBnb.project.AirBnbWebApp.exception.ResourceNotFoundException;
import com.airBnb.project.AirBnbWebApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserDto signUp(SignUpRequestDto singUpRequestDto){
      User user = userRepository.findByEmail(singUpRequestDto.getEmail()).orElse(null);

      if(user != null){
          throw new RuntimeException("User is already present with the same email");
      }
      User newUser = modelMapper.map(singUpRequestDto , User.class);
      newUser.setRoles(Set.of(Role.GUEST));
      newUser.setPassword(passwordEncoder.encode(singUpRequestDto.getPassword()));
      newUser = userRepository.save(newUser);

      return modelMapper.map(newUser, UserDto.class);
    }
    public String[] login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()
        ));

       User user = (User) authentication.getPrincipal();
       String[] arr = new String[2];
       arr[0] =jwtService.generateAccessToken(user);
       arr[1] = jwtService.generateAccessToken(user);
       return arr;
    }
    public String refreshToken(String refreshToken) {
        Long id = jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
        return jwtService.generateAccessToken(user);
    }
}
