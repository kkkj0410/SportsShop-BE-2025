package com.teamproject.back.service;

import com.teamproject.back.dto.UserDto;
import com.teamproject.back.entity.Role;
import com.teamproject.back.entity.Users;
import com.teamproject.back.repository.UserRepository;
import com.teamproject.back.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean save(UserDto userDto){
        if(this.findByUser(userDto.getEmail()) != null){
            log.info("User already exists");
            return false;
        }

        log.info("userDto : {}", userDto);
        userDto.setRole(Role.USER);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Users users = userRepository.save(userDtoToUser(userDto));
        log.info("Save User : {}", users);
        return true;
    }


    public UserDto findByUser(String email){
        Users users = userRepository.findByEmail(email);
        if(users == null){
            log.info("user is null");
            return null;
        }

        return usersToUserDto(users);
    }

    public int patchUser(UserDto userDto){
        return userRepository.patchUser(userDtoToUser(userDto));
    }

    public int patchPassword(String password){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.patchPassword(email, passwordEncoder.encode(password));
    }

    public int patchPassword(String email, String password){
        return userRepository.patchPassword(email, passwordEncoder.encode(password));
    }

//    private Users encryptUsers(Users users){
//        return Users.builder()
//                .id(users.getId())
//                .email(users.getEmail())
//                .password(users.getPassword())
//                .username(users.getUsername())
//                .phoneNumber(users.getPhoneNumber())
//                .role(users.getRole())
//                .birthday(users.getBirthday())
//                .build();
//
//    }
//
//    private Users decryptUsers(Users users){
//        return Users.builder()
//                .id(users.getId())
//                .email(users.getEmail())
//                .password(users.getPassword())
//                .username(users.getUsername())
//                .phoneNumber(users.getPhoneNumber())
//                .role(users.getRole())
//                .birthday(users.getBirthday())
//                .build();
//
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


    private Users userDtoToUser(UserDto userDto){
        return Users.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .username(userDto.getUsername())
                .phoneNumber(userDto.getPhoneNumber())
                .role(userDto.getRole())
                .birthday(userDto.getBirthday())
                .build();
    }


    public List<UserDto> findAllUsersList(int page, int size) {
            List<Users> usersList = userRepository.findAllUsers(page,size);
            List<UserDto> userDtos = new ArrayList<>();
            for(Users users : usersList){
                userDtos.add(usersToUserDto(users));
            }
            return userDtos;
    }
    //전체 사람수
    public int userCount(){
        int result  = userRepository.userCount();
        return result;
    }

    public UserDto findByUserId(Long id) {
        Users users = userRepository.findById(id);
        return usersToUserDto(users);
    }
}
