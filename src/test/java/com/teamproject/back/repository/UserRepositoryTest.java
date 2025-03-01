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

class UserRepositoryTest {

    UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Test
    public void 사용자가저장되어있지않다면저장됨(){
        //given
        Users user =  Users.builder()
                .email("email1")
                .password("password1")
                .username("username1")
//                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        userRepository.save(user);

        //then
        Users findUser = userRepository.findByEmail(user.getEmail());
        assertEquals(user.getEmail(),findUser.getEmail());
        assertEquals(user.getPassword(),findUser.getPassword());
        assertEquals(user.getUsername(),findUser.getUsername());
//        assertEquals(user.getPhoneNumber(),findUser.getPhoneNumber());
        assertEquals(user.getBirthday(),findUser.getBirthday());
    }

    @Test
    public void 사용자가저장되어있다면저장되지않음(){
        //given
        Users user =  Users.builder()
                .email("email1")
                .password("password1")
                .username("username1")
//                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        userRepository.save(user);
        userRepository.save(user);

        //then
        Users findUser = userRepository.findByEmail(user.getEmail());
        assertEquals(user,findUser);
    }


    @Test
    public void 사용자가저장되어있다면수정가능(){
        //given
        Users user =  Users.builder()
                .email("email1")
                .password("password1")
                .username("username1")
//                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        userRepository.save(user);
        Users updatedUser =  Users.builder()
                .email("email1")
                .username("updatedUsername")
//                .phoneNumber("updatedNumber")
                .role(Role.USER)
                .birthday(LocalDate.of(2023,9,20))
                .build();
        userRepository.patchUser(updatedUser);


        //then
        Users findUser = userRepository.findByEmail(user.getEmail());
        assertEquals(updatedUser.getUsername(),findUser.getUsername());
//        assertEquals(updatedUser.getPhoneNumber(),findUser.getPhoneNumber());
        assertEquals(updatedUser.getBirthday(),findUser.getBirthday());
    }

    @Test
    public void 사용자가저장되어있지않다면수정불가(){
        //given
        Users user =  Users.builder()
                .email("email1")
                .password("password1")
                .username("username1")
//                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        userRepository.patchUser(user);


        //then
        Users findUser = userRepository.findByEmail(user.getEmail());
        assertNull(findUser);
    }

    @Test
    public void 사용자가저장돼있다면비밀번호수정가능(){
        //given
        Users user =  Users.builder()
                .email("email1")
                .password("password1")
                .username("username1")
//                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();
        //when
        String updatedPassword = "updatedPassword";
        userRepository.save(user);
        userRepository.patchPassword(user.getEmail(), updatedPassword);

        //then
        Users findUser = userRepository.findByEmail(user.getEmail());
        assertEquals(findUser.getPassword(), updatedPassword);
    }

    @Test
    public void 사용자가저장돼있지않다면비밀번호수정불가(){
        //given
        Users user =  Users.builder()
                .email("email1")
                .password("password1")
                .username("username1")
//                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                .birthday(LocalDate.of(2002,6,23))
                .build();

        //when
        userRepository.patchPassword(user.getEmail(), user.getPassword());

        //then
        Users findUser = userRepository.findByEmail(user.getEmail());
        assertNull(findUser);
    }
}