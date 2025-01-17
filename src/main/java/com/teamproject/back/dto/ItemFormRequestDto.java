package com.teamproject.back.dto;

import com.teamproject.back.entity.Cart;
import com.teamproject.back.entity.Category;
import com.teamproject.back.entity.Order;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@Builder
public class ItemFormRequestDto {

    private Integer id;

    private String itemName;

    private String itemDesc;

    private String itemImg;

    private int itemStock;

    private int itemOriginPrice;

    private String itemBrand;

    private Category category;

    private MultipartFile imageFile;

}
