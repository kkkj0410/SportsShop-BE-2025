package com.teamproject.back.dto;

import com.teamproject.back.entity.Category;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
//상품 생성 및 수정
public class ItemFormResponseDto {

    private Integer id;

    private String itemName;

    private String itemDesc;

    //이미지 URL
    private String itemImg;

    private String itemRating;

    private int itemStock;

    private int itemOriginPrice;

    private String itemBrand;

    private Category category;

}
