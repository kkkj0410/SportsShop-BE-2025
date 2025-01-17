package com.teamproject.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.back.config.TestServiceConfig;
import com.teamproject.back.dto.UserDto;
import com.teamproject.back.entity.Role;
import com.teamproject.back.jwt.JwtTokenProvider;
import com.teamproject.back.mock.TestSecurityConfig;
import com.teamproject.back.service.AuthService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AuthController.class,
    excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class)
            })
@ImportAutoConfiguration(TestSecurityConfig.class)
@Import(TestServiceConfig.class)
class AuthControllerTest {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public AuthControllerTest(AuthService authService, JwtTokenProvider jwtTokenProvider, MockMvc mvc) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mvc = mvc;
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(authService);
    }

    @Test
    public void 사용자가저장돼있다면로그인() throws Exception {
        //given
        UserDto userDto =  UserDto.builder()
                .email("email1")
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
//                .birthday(LocalDate.of(2002,6,23))
                .build();


        //when
        when(authService.login(userDto.getEmail(), userDto.getPassword())).thenReturn(userDto);
        when(jwtTokenProvider.createJwtCookie(userDto.getEmail(),userDto.getRole())).thenReturn(new Cookie("test","test"));

        //then
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void 사용자가저장돼있지않다면로그인안됨() throws Exception {
        //given
        UserDto userDto =  UserDto.builder()
                .email("email1")
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
//                .birthday(LocalDate.of(2002,6,23))
                .build();


        //when
        when(authService.login(userDto.getEmail(), userDto.getPassword())).thenReturn(null);
        when(jwtTokenProvider.createJwtCookie(userDto.getEmail(),userDto.getRole())).thenReturn(new Cookie("test","test"));

        //then
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto))
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    public void 사용자의비밀번호가맞다면승인() throws Exception {
        //given
        //given
        UserDto userDto =  UserDto.builder()
                .email("email1")
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
//                .birthday(LocalDate.of(2002,6,23))
                .build();

        //when
        when(authService.validatePassword(userDto.getPassword())).thenReturn(true);

        //then
        mvc.perform(post("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void 사용자의비밀번호가맞지않다면비승인() throws Exception {
        //given
        //given
        UserDto userDto =  UserDto.builder()
                .email("email1")
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
//                .birthday(LocalDate.of(2002,6,23))
                .build();

        //when
        when(authService.validatePassword(userDto.getPassword())).thenReturn(false);

        //then
        mvc.perform(post("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto))
                )
                .andExpect(status().isBadRequest());
    }

}