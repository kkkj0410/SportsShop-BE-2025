package com.teamproject.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.back.config.TestServiceConfig;
import com.teamproject.back.dto.UserDto;
import com.teamproject.back.entity.Role;
import com.teamproject.back.mock.TestSecurityConfig;
import com.teamproject.back.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class)
        })
@ImportAutoConfiguration(TestSecurityConfig.class)
@Import(TestServiceConfig.class)
class UserControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mvc;


    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void 사용자가저장돼있지않다면저장() throws Exception {
        //given
        UserDto userDto =  UserDto.builder()
                .email("email1")
                .password("password1")
                .username("username1")
                .phoneNumber("phoneNumber1")
                .role(Role.USER)
                //LocalDate 값을 보내려면 별도의 라이브러리 필요
//                .birthday(LocalDate.of(2002,6,23))
                .build();

        //when
        //then
        mvc.perform(post("/api/signup")
                    .contentType(MediaType.APPLICATION_JSON) // 요청 Content-Type 설정
                    .content(mapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "email1", roles = "USER")
    public void 사용자가있다면조회() throws Exception {
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
        when(userService.findByUser(anyString())).thenReturn(userDto);

        //then
        mvc.perform(get("/api/user")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void 사용자가있다면정보수정가능() throws Exception{
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
        when(userService.patchUser(any(UserDto.class))).thenReturn(1);

        //then
        mvc.perform(patch("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void 사용자가있다면비밀번호수정가능() throws Exception{
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
        when(userService.patchPassword(anyString())).thenReturn(1);

        //then
        mvc.perform(patch("/api/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk());
    }


}