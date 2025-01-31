
package com.teamproject.back.entity;

import jakarta.persistence.*;

@Table
@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    private String city;

    private String street;

    private String zipcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;
}

