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

    //**3.7**(Integer -> Long 으로 디자인 변경, 소수점 1자리만 표기)
    private Integer averageRating;

    private int itemStock;

    private int itemPrice;

    private int itemOriginPrice;

    private String itemBrand;

    private int totalData;
}
