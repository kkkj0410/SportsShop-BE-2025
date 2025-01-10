package com.teamproject.back.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table
@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime orderDate;

    private String orderStatus;

    private int amount;

    private char payStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
