package com.teamproject.back.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class TestController {

    @GetMapping("/security/user")
    public ResponseEntity<String> user(){
//        SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("name : {}", SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/security/admin")
    public ResponseEntity<String> admin(){
        return ResponseEntity.ok("ok12");
    }
}
