package com.teamproject.back.controller;

import com.teamproject.back.dto.UserDto;
import com.teamproject.back.jwt.JwtTokenProvider;
import com.teamproject.back.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

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
    public ResponseEntity<String> loginPost(@RequestBody UserDto userDto, HttpServletResponse response) {
        log.info("userDto : {}", userDto);

        userDto = authService.login(userDto.getEmail(), userDto.getPassword());
        if(userDto != null){
            response.addCookie(jwtTokenProvider.createJwtCookie(userDto.getEmail(), userDto.getRole()));
            return ResponseEntity.ok("Login successful");
        }

        return ResponseEntity.badRequest().body("조회되지 않는 회원입니다.");
    }

    @PostMapping("/password")
    public ResponseEntity<String> passwordPost(@RequestBody UserDto userDto) {
        log.info("password : {}", userDto.getPassword());


        if(authService.validatePassword(userDto.getPassword())){
            return ResponseEntity.ok("맞는 패스워드입니다");
        }
        return ResponseEntity.badRequest().body("옳지않은 패스워드입니다");

    }


}
