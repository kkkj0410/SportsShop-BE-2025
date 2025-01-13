package com.teamproject.back.service;

import com.teamproject.back.dto.UserDto;
import com.teamproject.back.entity.Users;
import com.teamproject.back.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthRepository authRepository, BCryptPasswordEncoder passwordEncoder){
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto login(String email, String inputPassword){
        UserDto userDto = findByUser(email);

        if(userDto != null && this.validatePassword(inputPassword, userDto.getPassword())){
            return userDto;
        }
        return null;
    }

    public UserDto findByUser(String email){
        Users user = authRepository.findByEmail(email);
        if(user != null){
            return usersToUserDto(user);
        }

        return null;
    }

    public boolean validatePassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public boolean validatePassword(String rawPassword){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return passwordEncoder.matches(rawPassword, this.findByUser(email).getPassword());
    }


//    // 비밀번호 검증
//    public boolean validatePassword(String inputPassword, String email) {
//        UserDto userDto = this.findByUser(email);
//
//        return passwordEncoder.matches(inputPassword, userDto.getPassword());
//    }

    private UserDto usersToUserDto(Users users){
        return UserDto.builder()
                .id(users.getId())
                .email(users.getEmail())
                .password(users.getPassword())
                .username(users.getUsername())
                .phoneNumber(users.getPhoneNumber())
                .role(users.getRole())
                .birthday(users.getBirthday())
                .build();
    }

}
