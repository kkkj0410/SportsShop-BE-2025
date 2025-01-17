package com.teamproject.back.service;

import com.teamproject.back.config.TestRepositoryConfig;
import com.teamproject.back.dto.UserDto;
import com.teamproject.back.entity.Role;
import com.teamproject.back.entity.Users;
import com.teamproject.back.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@Slf4j
@Import(TestRepositoryConfig.class)
class UserServiceTest {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceTest(UserService userService, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(userRepository);
    }

    @Test
    public void 사용자가저장되어있지않다면저장됨(){
        //given
        Users user =  Users.builder()
                .email("email1")
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();

        //when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        //then
        UserDto findUserDto = userService.findByUser(user.getEmail());

        log.info("userDto.getPassword() : {}", user.getPassword());
        log.info("findUserDto.getPassword() : {}", findUserDto.getPassword());

        assertEquals(user.getEmail(),findUserDto.getEmail());
        assertEquals(user.getPassword(),findUserDto.getPassword());
        assertEquals(user.getUsername(),findUserDto.getUsername());
        assertEquals(user.getPhoneNumber(),findUserDto.getPhoneNumber());
        assertEquals(user.getBirthday(),findUserDto.getBirthday());
    }

    @Test
    public void 사용자를저장할때비밀번호암호화됨(){
        //given
        String password = "password1";
        String encodedPassword  = "encodedPassword";
        Users user =  Users.builder()
                .email("email1")
                .password(password)
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        Users encodedUser =  Users.builder()
                .email("email1")
                .password(encodedPassword)
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();

        //when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(encodedUser);

        //then
        UserDto findUserDto = userService.findByUser(user.getEmail());
        assertNotEquals(password,findUserDto.getPassword());

    }


//    @Test
//    public void 사용자가저장되어있다면수정가능(){
//        //given
//        Users updatedUser =  Users.builder()
//                .email("email1")
//                .password("updatedPassword1")
//                .username("updatedUsername1")
//                .phoneNumber("updatedPhoneNumber1")
//                .role(Role.USER)
//                .birthday(LocalDate.of(2002,6,23))
//                .build();
//        //when
////        userService.save(userDto);
//        UserDto updatedUserDto =  UserDto.builder()
//                .email("email1")
//                .password("updatedPassword1")
//                .username("updatedUsername1")
//                .phoneNumber("updatedPhoneNumber1")
//                .role(Role.USER)
//                .birthday(LocalDate.of(2002,6,23))
//                .build();
//
//        when(userRepository.patchUser(updatedUser)).thenReturn(1);
//
//        //then
//        assertEquals(1, userService.patchUser(updatedUserDto));
////        UserDto findUserDto = userService.findByUser(userDto.getEmail());
////        assertNotEquals(userDto.getUsername(),findUserDto.getUsername());
////        assertNotEquals(userDto.getPhoneNumber(),findUserDto.getPhoneNumber());
////        assertNotEquals(userDto.getBirthday(),findUserDto.getBirthday());
//    }


//    @Test
//    public void 사용자가저장되어있지않다면수정불가능(){
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
//        userService.patchUser(userDto);
//
//        //then
//        UserDto findUserDto = userService.findByUser(userDto.getEmail());
//        assertNull(findUserDto);
//    }
//
//
//    @Test
//    @WithMockUser(username = "email1", roles = "USER")
//    public void 사용자가저장돼있다면비밀번호수정가능(){
//
//        //given
//        UserDto userDto =  UserDto.builder()
//                .email(SecurityContextHolder.getContext().getAuthentication().getName())
//                .password("password1")
//                .username("username1")
//                .phoneNumber("phoneNumber1")
//                .role(Role.USER)
//                .birthday(LocalDate.of(2002,6,23))
//                .build();
//        //when
//        userService.save(userDto);
//        String updatedPassword = "updatedPassword";
//        userService.patchPassword(updatedPassword);
//
//        //then
//        UserDto findUserDto = userService.findByUser(userDto.getEmail());
//        log.info("findUserDto : {}", findUserDto);
//
//        assertNotEquals(userDto.getPassword(), findUserDto.getPassword());
//    }
//
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
//        assertNull(findUserDto);
//    }


}