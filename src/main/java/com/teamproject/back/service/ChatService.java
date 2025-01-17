package com.teamproject.back.service;

import com.teamproject.back.dto.ChatDTO;
import com.teamproject.back.entity.Chats;
import com.teamproject.back.entity.Users;
import com.teamproject.back.repository.ChatRepository;
import com.teamproject.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    public void saveChat(ChatDTO chatDTO) {
        chatRepository.saveChat(dtoToEntity(chatDTO));

    }


    public Chats dtoToEntity(ChatDTO chatDTO) {
        Users sender = userRepository.findByEmail(chatDTO.getUsername());
        Chats chats = new Chats();
        chats.setSender(sender);
        chats.setRecipient();
        chats.setMessage(chatDTO.getMessage());
        chats.setFromAdmin(chatDTO.isFromAdmin());
        chats.setSendAt(chatDTO.getSentAt());
        chats.setReadCheck(chatDTO.isReadCheck());

        return chats;
    }

    public List<String> findByAllChatList() {
        return chatRepository.findByAllChatList();
    }

    public List<ChatDTO> getRecentMessages(String decodeUserName, int i) {
    }
}
