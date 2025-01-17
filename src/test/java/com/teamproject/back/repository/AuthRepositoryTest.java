package com.teamproject.back.repository;

import com.teamproject.back.entity.Role;
import com.teamproject.back.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class AuthRepositoryTest {
    
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthRepositoryTest(AuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
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
        userRepository.save(user);

        //then
        Users findUser = authRepository.findByEmail(user.getEmail());
        assertEquals(user.getUsername(),findUser.getUsername());
        assertEquals(user.getPhoneNumber(),findUser.getPhoneNumber());
        assertEquals(user.getBirthday(),findUser.getBirthday());
        
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

        //then
        Users findUser = authRepository.findByEmail(user.getEmail());
        assertNull(findUser);

    }
}