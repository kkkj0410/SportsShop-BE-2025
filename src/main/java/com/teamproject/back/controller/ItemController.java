package com.teamproject.back.controller;


import com.teamproject.back.dto.ItemFormResponseDto;
import com.teamproject.back.dto.ItemFormRequestDto;
import com.teamproject.back.entity.Category;
import com.teamproject.back.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.Arrays;
import java.util.List;


@Slf4j
@RequestMapping("/api")
@Controller
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping("/item")
    public ResponseEntity<?> item(@ModelAttribute ItemFormRequestDto itemFormRequestDto){
        if(itemService.save(itemFormRequestDto) != null){
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.badRequest().body("상품 등록에 실패했습니다");
    }

    @DeleteMapping("/item")
    public ResponseEntity<String> itemDelete(@RequestParam("id") int id, @RequestParam("itemImg") String itemImg){

        if(itemService.deleteById(id, itemImg) == 1){
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.badRequest().body("삭제에 실패 했습니다.");
    }

    @PatchMapping("/item")
    public ResponseEntity<?> itemUpdate(@ModelAttribute ItemFormRequestDto itemFormRequestDto){

        if(itemService.updateItem(itemFormRequestDto) != null){
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.badRequest().body("수정에 실패 했습니다.");
    }

    @GetMapping("/item")
    public ResponseEntity<?> itemGet(@RequestParam("size") int size, @RequestParam("page") int page){
        //page = page-1 => page는 0부터 시작
        List<ItemFormResponseDto> itemDtoList = itemService.findItemList(size,page);
        if(itemDtoList != null){
            return ResponseEntity.ok(itemDtoList);
        }

        return ResponseEntity.badRequest().body("상품 조회에 실패 했습니다.");
    }


    @GetMapping("/item/{id}")
    public ResponseEntity<?> itemGetOne(@PathVariable("id") int id){

        ItemFormResponseDto itemFormResponseDto = itemService.findById(id);
        if(itemFormResponseDto != null){
            return ResponseEntity.ok(itemFormResponseDto);
        }

        return ResponseEntity.badRequest().body("상품 조회에 실패 했습니다.");
    }

}
