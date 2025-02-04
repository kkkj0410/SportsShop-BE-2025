package com.teamproject.back.entity;

import com.teamproject.back.util.AesUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
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

    private LocalDate birthday;

    @Column(name = "enroll_date", nullable = false, updatable = false)
    private LocalDateTime enrollDate;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @OneToMany(mappedBy = "users")
    private List<Order> orders;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "users")
    private List<Address> addresses;

    @OneToMany(mappedBy = "users")
    private List<Cart> carts;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Likes> likes;

    @PrePersist
    public void prePersist() {
        if (this.enrollDate == null) {
            this.enrollDate = LocalDateTime.now();
        }
        encryptField();
    }

    @PostLoad
    public void postLoad(){
        decryptField();
    }

    @PreUpdate
    public void preUpdate(){
        encryptField();
    }

    public void deleteUser() {
        this.deleteDate = LocalDateTime.now();
    }

    private void encryptField(){
        this.email = AesUtil.encrypt(email);
        this.username = AesUtil.encrypt(username);
        this.phoneNumber = AesUtil.encrypt(phoneNumber);
    }

    private void decryptField(){
        this.email = AesUtil.decrypt(email);
        this.username = AesUtil.decrypt(username);
        this.phoneNumber = AesUtil.decrypt(phoneNumber);
    }

}
