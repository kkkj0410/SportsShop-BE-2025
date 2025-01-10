package com.teamproject.back.entity;

import jakarta.persistence.*;

@Entity
public class Cart {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "item_id",nullable = false)
    private Item item;
}
