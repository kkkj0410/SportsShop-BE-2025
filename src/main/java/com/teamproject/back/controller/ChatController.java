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
import org.springframework.web.bind.annotation.PathVariable;
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
        String roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().trim().replace("[", "").replace("]", "");
        chatDTO.setRoomName(decodeUserName);
        chatDTO.setSender(sender); // 발신자 설정
        log.info("sender : {}",sender);
        chatDTO.setSender(sender);
        if(roles.equals("ROLE_ADMIN")) {
            //ADMIN 일 경우
            chatDTO.setFromAdmin(true);
            chatDTO.setSender(sender);
        }else{
            //USER 일 경우
            chatDTO.setFromAdmin(false);
            chatDTO.setSender(sender);
        }
        //
        log.info("chat{}",chatDTO);
        chatDTO.setSentAt(LocalDateTime.now()); // 메시지 전송 시간 설정
        chatDTO.setReadCheck(false); // default로 false
        chatService.saveChat(chatDTO); // 서비스 로직
        messagingTemplate.convertAndSend("/sub/chat/" + decodeUserName,chatDTO);
    }
    //admin이 사용자가 보낸 메세지 방들을 출력하는 로직
    @GetMapping("/api/admin/chatlist")
    public ResponseEntity<List<String>> getChatList() {
        log.error("권한정보 {}",SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        List<String> list = chatService.findByAllChatList();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/api/chat/{decodeUserName}")
    public ResponseEntity<List<ChatDTO>> getChat(@PathVariable String decodeUserName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ChatDTO> previousMessages = chatService.getRecentMessages(decodeUserName, 10,username);
        return ResponseEntity.ok(previousMessages);
    }

}

