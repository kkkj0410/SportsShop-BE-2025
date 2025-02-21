package com.teamproject.back.controller;

import com.teamproject.back.dto.UserDto;
import com.teamproject.back.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@RequestMapping("/api")
@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signupRequest(@RequestBody UserDto userDto){
        log.info("userDto : {}", userDto);
        userService.save(userDto);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email){

        log.info("email : {}", email);
        UserDto userDto = userService.findByUser(email);

        if(userDto != null){
            return ResponseEntity.ok(userDto);
        }

        return ResponseEntity.badRequest().body("조회되지 않는 회원입니다.");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserByCookie(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("email : {}", email);
        UserDto userDto = userService.findByUser(email);

        if(userDto != null){
            return ResponseEntity.ok(userDto);
        }

        return ResponseEntity.badRequest().body("조회되지 않는 회원입니다.");
    }

    @PatchMapping("/user")
    public ResponseEntity<?> userPatch(@RequestBody UserDto userDto){
        if(userService.patchUser(userDto) == 1){
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.badRequest().body("사용자 수정 실패");
    }

    @PatchMapping("/user/profile/username")
    public ResponseEntity<?> usernamePatch(@RequestBody UserDto userDto){
        log.info("username : {}", userDto.getUsername());

        if(userService.patchUsername(userDto.getUsername()) == 1){
            return ResponseEntity.ok("ok");
        }


        return ResponseEntity.badRequest().body("이름 수정 실패");
    }

    @PatchMapping("/user/profile/password")
    public ResponseEntity<?> passwordPatch(@RequestBody UserDto userDto){
        if(userService.patchPassword(userDto.getPassword()) == 1){
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.badRequest().body("비밀번호 수정 실패");
    }


    //forgot Password
    @PatchMapping("/user/password")
    public ResponseEntity<?> patchUserPassword(@RequestBody UserDto userDto){
        if(userService.patchPassword(userDto.getEmail(), userDto.getPassword()) == 1){
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.badRequest().body("비밀번호 수정 실패");
    }


}
