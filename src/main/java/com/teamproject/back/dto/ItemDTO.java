package com.teamproject.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemDTO {
    private int id;

    private String itemName;

    private String itemDesc;

    private String itemImg;

    private String itemRating;

    private int itemStock;

    private int itemPrice;

    private int itemOriginPrice;

    private String itemBrand;
}
