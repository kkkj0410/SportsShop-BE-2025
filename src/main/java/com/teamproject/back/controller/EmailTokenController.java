package com.teamproject.back.controller;


import com.teamproject.back.dto.EmailTokenDTO;
import com.teamproject.back.service.EmailTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class EmailTokenController {

    private final EmailTokenService emailTokenService;

    @Autowired
    public EmailTokenController(EmailTokenService emailTokenService) {
        this.emailTokenService = emailTokenService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> postEmail(@RequestBody EmailTokenDTO emailTokenDTO){
        if(!emailTokenService.sendEmail(emailTokenDTO.getEmail())){
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/auth/email")
    public ResponseEntity<String> postAuthEmail(@RequestBody EmailTokenDTO emailTokenDTO){
        if(!emailTokenService.vertify(emailTokenDTO)){
            return ResponseEntity.badRequest().body("이메일 토큰 인증 실패");
        }

        return ResponseEntity.ok("ok");
    }

}
