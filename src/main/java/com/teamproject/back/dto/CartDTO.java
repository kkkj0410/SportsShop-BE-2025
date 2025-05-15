package com.teamproject.back.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDTO {
    private Long id;
    private Integer quantity;

    private Integer itemId;
    private String itemName;
    private String itemImg;
    private Integer itemPrice;
}
