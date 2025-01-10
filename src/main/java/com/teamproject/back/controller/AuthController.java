package com.teamproject.back.controller;

import com.teamproject.back.dto.UserDto;
import com.teamproject.back.jwt.JwtTokenProvider;
import com.teamproject.back.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // login
    // logout

    // login 검증 및 JWT 토큰 발급
    @PostMapping("/login")
    public ResponseEntity<String> loginRequest(@RequestBody UserDto userDto, HttpServletResponse response) {
        log.info("userDto : {}", userDto);

        userDto = authService.findByUser(userDto.getEmail());
        if(userDto == null){
            return ResponseEntity.badRequest().body("조회되지 않는 회원입니다.");
        }

        response.addCookie(jwtTokenProvider.createJwtCookie(userDto.getEmail(), userDto.getRole()));
        return ResponseEntity.ok("Login successful");
    }


}
