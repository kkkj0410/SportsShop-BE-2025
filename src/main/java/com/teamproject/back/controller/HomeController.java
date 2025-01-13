package com.teamproject.back.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/api/home")
    public ResponseEntity<String>homeController(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("name:{}",username);
        return ResponseEntity.ok("username: " + username);
    }
}
