package com.teamproject.back.config;


import com.teamproject.back.jwt.JwtTokenProvider;
import com.teamproject.back.repository.AuthRepository;
import com.teamproject.back.repository.UserRepository;
import com.teamproject.back.service.AuthService;
import com.teamproject.back.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestServiceConfig {
    @Bean
    @Primary
    public AuthService authService(){
        return mock(AuthService.class);
    }

    @Bean
    @Primary
    public UserService userService(){
        return mock(UserService.class);
    }

    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider(){
        return mock(JwtTokenProvider.class);
    }

}
