package com.teamproject.back.controller;

import com.teamproject.back.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatDTO chatDTO) {
        // user getName
        chatDTO.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        chatDTO.setSentAt(LocalDateTime.now());  // 메시지 전송 시간 설정
        log.info(chatDTO.toString());
        // 채팅 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/" + chatDTO.getUserId(), chatDTO);
    }
}
