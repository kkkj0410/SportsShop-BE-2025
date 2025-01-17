package com.teamproject.back.service;

import com.teamproject.back.config.TestRepositoryConfig;
import com.teamproject.back.dto.UserDto;
import com.teamproject.back.entity.Role;
import com.teamproject.back.entity.Users;
import com.teamproject.back.repository.AuthRepository;
import com.teamproject.back.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


//SpringBootTest : 스프링 빈 주입받기
//Import : 테스트 코드에 있는 스프링 빈은 스캔 대상X(따라서, 테스트 코드에 있는 스프링 빈 사용하고 싶으면 별도로 Import)
@SpringBootTest
@Import(TestRepositoryConfig.class)
class AuthServiceTest {

    private final AuthRepository authRepository;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceTest(AuthRepository authRepository, AuthService authService, BCryptPasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(authRepository);
    }

    @Test
    public void 사용자가저장되어있다면로그인가능(){
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
        when(authRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);

        //then
        UserDto findUserDto = authService.login(user.getEmail(), user.getPassword());
        assertEquals(findUserDto.getEmail(), user.getEmail());
    }


    @Test
    public void 사용자가저장되어있지않다면로그인불가능(){
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
        when(authRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);

        //then
        UserDto findUserDto = authService.login(user.getEmail(), user.getPassword());
        assertNull(findUserDto);
    }

    @Test
    public void 사용자가저장되어있다면조회가능(){
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
        when(authRepository.findByEmail(user.getEmail())).thenReturn(user);

        //then
        UserDto findUserDto = authService.findByUser(user.getEmail());
        assertEquals(findUserDto.getEmail(), user.getEmail());
    }

    @Test
    public void 사용자가저장되어있지않다면조회불가능(){
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
        when(authRepository.findByEmail(user.getEmail())).thenReturn(null);

        //then
        UserDto findUserDto = authService.findByUser(user.getEmail());
        assertNull(findUserDto);
    }
}