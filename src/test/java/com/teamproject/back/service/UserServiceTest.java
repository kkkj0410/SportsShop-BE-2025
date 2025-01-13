package com.teamproject.back.service;

import com.teamproject.back.dto.UserDto;
import com.teamproject.back.entity.Role;
import com.teamproject.back.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
@Transactional
class UserServiceTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void 사용자가저장되어있지않다면저장됨(){
        //given
        UserDto userDto =  UserDto.builder()
                .email("email1")
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        userService.save(userDto);

        //then
        UserDto findUserDto = userService.findByUser(userDto.getEmail());

        log.info("userDto.getPassword() : {}", userDto.getPassword());
        log.info("findUserDto.getPassword() : {}", findUserDto.getPassword());

        assertEquals(userDto.getEmail(),findUserDto.getEmail());
        assertEquals(userDto.getPassword(),findUserDto.getPassword());
        assertEquals(userDto.getUsername(),findUserDto.getUsername());
        assertEquals(userDto.getPhoneNumber(),findUserDto.getPhoneNumber());
        assertEquals(userDto.getBirthday(),findUserDto.getBirthday());
    }

    @Test
    public void 사용자를저장할때비밀번호암호화됨(){
        //given
        String password = "password1";
        UserDto userDto =  UserDto.builder()
                .email("email1")
                .password(password)
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        userService.save(userDto);

        //then
        UserDto findUserDto = userService.findByUser(userDto.getEmail());
        assertNotEquals(password,findUserDto.getPassword());

    }

    @Test
    public void 사용자가저장되어있다면저장되지않음(){
        //given
        UserDto userDto =  UserDto.builder()
                .email("email1")
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        userService.save(userDto);
        userService.save(userDto);

        //then
        UserDto findUserDto = userService.findByUser(userDto.getEmail());

        assertEquals(userDto.getEmail(),findUserDto.getEmail());
        assertEquals(userDto.getPassword(),findUserDto.getPassword());
        assertEquals(userDto.getUsername(),findUserDto.getUsername());
        assertEquals(userDto.getPhoneNumber(),findUserDto.getPhoneNumber());
        assertEquals(userDto.getBirthday(),findUserDto.getBirthday());
    }


    @Test
    public void 사용자가저장되어있다면수정가능(){
        //given
        UserDto userDto =  UserDto.builder()
                .email("email1")
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        userService.save(userDto);
        UserDto updatedUserDto =  UserDto.builder()
                .email("email1")
                .password("updatedPassword1")
                .username("updatedUsername1")
                .phoneNumber("updatedPhoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2502,11,11))
                .build();

        userService.patchUser(updatedUserDto);

        //then
        UserDto findUserDto = userService.findByUser(userDto.getEmail());
        assertNotEquals(userDto.getUsername(),findUserDto.getUsername());
        assertNotEquals(userDto.getPhoneNumber(),findUserDto.getPhoneNumber());
        assertNotEquals(userDto.getBirthday(),findUserDto.getBirthday());
    }


    @Test
    public void 사용자가저장되어있지않다면수정불가능(){
        //given
        UserDto userDto =  UserDto.builder()
                .email("email1")
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        userService.patchUser(userDto);

        //then
        UserDto findUserDto = userService.findByUser(userDto.getEmail());
        assertNull(findUserDto);
    }


    @Test
    @WithMockUser(username = "email1", roles = "USER")
    public void 사용자가저장돼있다면비밀번호수정가능(){

        //given
        UserDto userDto =  UserDto.builder()
                .email(SecurityContextHolder.getContext().getAuthentication().getName())
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        userService.save(userDto);
        String updatedPassword = "updatedPassword";
        userService.patchPassword(updatedPassword);

        //then
        UserDto findUserDto = userService.findByUser(userDto.getEmail());
        log.info("findUserDto : {}", findUserDto);

        assertNotEquals(userDto.getPassword(), findUserDto.getPassword());
    }

//    @Test
//    @WithMockUser(username = "email1", roles = "USER")
//    public void 사용자가저장돼있지않다면비밀번호수정불가(){
//        //given
//        UserDto userDto =  UserDto.builder()
//                .email("email1")
//                .password("password1")
//                .username("username1")
//                .phoneNumber("phoneNumber1")
//                .role(Role.USER)
//                .birthday(LocalDate.of(2002,6,23))
//                .build();
//        //when
//        String updatedPassword = "updatedPassword";
//        userService.patchPassword(updatedPassword);
//
//        //then
//        UserDto findUserDto = userService.findByUser(userDto.getEmail());
//        assertNotEquals(userDto.getPassword(), findUserDto.getPassword());
//    }



}