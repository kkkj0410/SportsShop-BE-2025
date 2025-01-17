package com.teamproject.back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Data
@Table(name = "item")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
