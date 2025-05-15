package com.teamproject.back.service;

import com.teamproject.back.dto.ChatDTO;
import com.teamproject.back.entity.Chats;
import com.teamproject.back.entity.Users;
import com.teamproject.back.repository.ChatRepository;
import com.teamproject.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    @Async
    public void saveChat(ChatDTO chatDTO) {
        chatRepository.saveChat(dtoToEntity(chatDTO));

    }


    public Chats dtoToEntity(ChatDTO chatDTO) {
        Users sender = userRepository.findByEmail(chatDTO.getSender()); //보낸사람
        Chats chats = new Chats();
        chats.setUsers(sender);
        chats.setRoomName(chatDTO.getRoomName()); //방이름 인코딩한값을 날려줄거임
        chats.setMessage(chatDTO.getMessage());
        chats.setFromAdmin(chatDTO.isFromAdmin());
        chats.setSendAt(chatDTO.getSentAt());
        chats.setReadCheck(chatDTO.isReadCheck());
        return chats;
    }

    public List<String> findByAllChatList() {
        return chatRepository.findByAllChatList();
    }

    public ChatDTO entityToDto(Chats chats){
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setSender(chats.getUsers().getUsername());
        chatDTO.setMessage(chats.getMessage());
        chatDTO.setFromAdmin(chats.isFromAdmin());
        chatDTO.setSentAt(chats.getSendAt());
        chatDTO.setReadCheck(chats.isReadCheck());
        return chatDTO;
    }

    public List<ChatDTO> getRecentMessages(String decodeUserName, int i,String username) {
        ChatDTO chatDTO = new ChatDTO();
        Users user = userRepository.findByEmail(username); //사람
        List<Chats> chatsList = chatRepository.findByUserChatList(decodeUserName,i);
        List<ChatDTO> chatDTOList = new ArrayList<>();

        for(Chats chats : chatsList){
            if(chats.getUsers().getId().equals(user.getId())){
                ChatDTO chatDto = entityToDto(chats);
                chatDto.setMyself(true);
                chatDTOList.add(chatDto);
            }else{
                ChatDTO chatDto = entityToDto(chats);
                chatDto.setMyself(false);
                chatDTOList.add(chatDto);
            }
        }
        log.error("{}", chatDTOList);
        return chatDTOList;
    }
}
