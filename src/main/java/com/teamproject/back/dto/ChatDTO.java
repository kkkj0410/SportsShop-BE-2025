package com.teamproject.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {
    private Long messageId;       // 메시지 ID
    private String userId;          // 사용자 ID
    private String username;      // 사용자 이름 (닉네임)
    private String message;       // 메시지 내용
    private boolean isFromAdmin;  // 관리자 발신 여부
    private LocalDateTime sentAt; // 메시지 전송 시간
}
