package com.teamproject.back.service;

import com.teamproject.back.dto.UserDto;
import com.teamproject.back.entity.Users;
import com.teamproject.back.repository.AuthRepository;
import com.teamproject.back.util.AesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AesUtil aesUtil;

    @Autowired
    public AuthService(AuthRepository authRepository, BCryptPasswordEncoder passwordEncoder, AesUtil aesUtil){
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.aesUtil = aesUtil;
    }

    public UserDto login(String email, String inputPassword){
        UserDto userDto = this.findByUser(email);

        if(userDto != null && this.validatePassword(inputPassword, userDto.getPassword())){
            return userDto;
        }
        return null;
    }

    public UserDto findByUser(String email){
        Users user = authRepository.findByEmail(aesUtil.encrypt(email));
        if(user != null){
            Users decryptUsers = decrypt(user);
            return usersToUserDto(decryptUsers);
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

    private Users decrypt(Users users){
        return Users.builder()
                .id(users.getId())
                .email(aesUtil.decrypt(users.getEmail()))
                .password(users.getPassword())
                .username(aesUtil.decrypt(users.getUsername()))
                .phoneNumber(aesUtil.decrypt(users.getPhoneNumber()))
                .role(users.getRole())
                .birthday(users.getBirthday())
                .build();

    }


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
