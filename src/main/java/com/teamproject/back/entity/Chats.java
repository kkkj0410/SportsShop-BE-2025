package com.teamproject.back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chats")
public class Chats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName; //암호화작업할거다

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id", nullable = false)
    private Users users; // 메시지를 보낸 사용자

    @Column(nullable = false)
    private boolean isFromAdmin; // 메시지 발신자 (true: 관리자, false: 사용자)

    @Column(nullable = false)
    private String message; // 메시지 내용

    @Column(name = "send_at" ,nullable = false)
    private LocalDateTime sendAt;

    @Column(name = "read_check")
    private boolean readCheck;
}
