package com.teamproject.back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String username;
    //010-1234-5678
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;

    //수정(1.9)
    //LocalDateTime -> LocalDate
    private LocalDate birthday;

    @Column(name = "enroll_date", nullable = false, updatable = false)
    private LocalDateTime enrollDate;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;


    @OneToMany(mappedBy = "users")
    private List<Order> orders;

    @OneToMany(mappedBy = "users")
    private List<Comment> comments;

    @OneToMany(mappedBy = "users")
    private List<Address> addresses;

    @OneToMany(mappedBy = "users")
    private List<Cart> carts;

    @OneToMany(mappedBy = "users")
    private List<Chats> chats;

    @OneToMany(mappedBy = "sender")
    private List<Chats> sentChats; // 사용자가 보낸 메시지 목록

    @OneToMany(mappedBy = "recipient")
    private List<Chats> receivedChats; // 사용자가 받은 메시지 목록


    @PrePersist
    public void prePersist() {
        if (this.enrollDate == null) {
            this.enrollDate = LocalDateTime.now();
        }
    }

    public void deleteUser() {
        this.deleteDate = LocalDateTime.now();
    }
}
