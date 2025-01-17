package com.teamproject.back.config;


import com.teamproject.back.repository.AuthRepository;
import com.teamproject.back.repository.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestRepositoryConfig {

    @Bean
    @Primary
    public AuthRepository authRepository(){
        return mock(AuthRepository.class);
    }

    @Bean
    @Primary
    public UserRepository userRepository(){
        return mock(UserRepository.class);
    }

    @Bean
    @Primary
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return mock(BCryptPasswordEncoder.class);
    }

}
