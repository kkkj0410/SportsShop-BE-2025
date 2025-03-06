package com.teamproject.back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Table
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDetailImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer imgIndex;

    private String img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    public void fetchItem(Item item){
        this.item = item;

        if (item.getItemDetailImages() == null) {
            item.setItemDetailImages(new ArrayList<>());
        }
        this.item.getItemDetailImages().add(this);
    }
}
