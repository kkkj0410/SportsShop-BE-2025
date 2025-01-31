package com.teamproject.back.controller;

import com.teamproject.back.dto.LikeDto;
import com.teamproject.back.entity.Role;
import com.teamproject.back.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
@RequestMapping("/api/login/oauth2")
public class OAuth2Controller {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2Controller(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/google")
    public String google(){
        return "redirect:/oauth2/authorization/google";

//        return "redirect:/test";
    }

    @GetMapping("/naver")
    public String naver(){
        return "redirect:/oauth2/authorization/naver";
    }

//    @GetMapping("redirect/fail/{email}")
//    public ResponseEntity<String> failOAuth2(@PathVariable("email") String email){
//        return ResponseEntity.badRequest().body(email);
//    }

//    @GetMapping("redirect/success/{email}")
//    public ResponseEntity<String> successOAuth2(@PathVariable("email") String email, HttpServletResponse response){
//        response.addCookie(jwtTokenProvider.createJwtCookie(email, Role.USER));
//        return ResponseEntity.ok("Login successful");
//    }

    @GetMapping("redirect/fail/{email}")
    public ResponseEntity<String> failOAuth2(@PathVariable("email") String email){
        String redirectUrl = "http://localhost:3000/signup?email=" + email;

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @GetMapping("redirect/success/{email}")
    public ResponseEntity<String> successOAuth2(@PathVariable("email") String email, HttpServletResponse response){
        String redirectUrl = "http://localhost:3000";
        response.addCookie(jwtTokenProvider.createJwtCookie(email, Role.USER));
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }


}
