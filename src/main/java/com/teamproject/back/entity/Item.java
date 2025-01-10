package com.teamproject.back.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Table(name = "item")
@Entity
public class Item {
    @Id
    @GeneratedValue
    private int id;

    private String itemName;

    private String itemDesc;

    private String itemImg;

    private String itemRating;

    private int itemStock;

    private int itemPrice;

    private int itemOriginPrice;

    private String itemBrand;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "item")
    private List<Order> orders;

    @OneToMany(mappedBy = "item")
    private List<Cart> carts;
}
