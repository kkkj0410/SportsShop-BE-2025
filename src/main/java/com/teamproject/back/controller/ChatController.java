package com.teamproject.back.controller;

import com.teamproject.back.dto.ChatDTO;
import com.teamproject.back.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    @MessageMapping("/chat/{decodeUserName}") // 채팅 보내는 경로 사용자의 대한것
    public void sendMessage(@DestinationVariable String decodeUserName, ChatDTO chatDTO) {
        String sender = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("받는 사람: {}", decodeUserName);
        log.info("보내는 사람: {}", sender);
        String roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        log.info("회원 정보 {}",roles);
        chatDTO.setUsername(sender); // 발신자 설정
        chatDTO.setSentAt(LocalDateTime.now()); // 메시지 전송 시간 설정
        chatDTO.setFromAdmin(roles.equals("ROLE_ADMIN")); // 발신자가 관리자일 경우 true
        chatDTO.setReadCheck(false);
        log.info("보낼 메시지: {}", chatDTO);
        chatService.saveChat(chatDTO); // 서비스 로직
        // 수신자 채팅 구독 경로로 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/" + decodeUserName, chatDTO);
    }
    //admin
    @GetMapping("/api/admin/chatlist")
    public ResponseEntity<List<String>> getChatList() {
        List<String> list = chatService.findByAllChatList();
        return ResponseEntity.ok(list);
    }
}

