package com.teamproject.back.controller;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
//@RequestMapping("/security")
public class TestController {

//    @GetMapping("/user")
//    public ResponseEntity<String> user(){
////        SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("name : {}", SecurityContextHolder.getContext().getAuthentication().getName());
//        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getName());
//    }
//
//    @GetMapping("/admin")
//    public ResponseEntity<String> admin(){
//        return ResponseEntity.ok("ok12");
//    }

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody Dto dto){
        log.info("dto : {}", dto);

        return ResponseEntity.ok("ok");
    }


    @Data
    static class Dto{
        String image;
    }

}
