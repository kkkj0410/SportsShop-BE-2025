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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_id", nullable = false)
    private Users sender; // 메시지를 보낸 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Users recipient; // 메시지를 보낸 사용자

    @Column(nullable = false)
    private boolean isFromAdmin; // 메시지 발신자 (true: 관리자, false: 사용자)

    @Column(nullable = false)
    private String message; // 메시지 내용

    @Column(name = "send_at" ,nullable = false)
    private LocalDateTime sendAt;

    @Column(name = "read_check")
    private boolean readCheck;
}
