package com.teamproject.back.controller;

import com.teamproject.back.dto.ItemDTO;
import com.teamproject.back.entity.Item;
import com.teamproject.back.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final ItemService itemService;

    // 삭제, 조회 기능
    //find/delete
    @GetMapping("/api/admin/item/find")
    public ResponseEntity<List<ItemDTO>> adminFindItem(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String itemName
    ) {
        log.info("아이템 이름{}", itemName);
        List<ItemDTO> itemList = new ArrayList<>();
        {
            itemList = itemService.searchItemList(page, size, itemName);
            return ResponseEntity.ok(itemList);

        }
    }
        //페이지 전반에 데이터를 가
        // 져옴
        @GetMapping("/api/admin/item/findAll")
        public ResponseEntity<List<ItemDTO>> adminFindAllItem (
        @RequestParam int page,
        @RequestParam int size
    ){
            List<ItemDTO> itemList = new ArrayList<>();
            log.info("page{}",page);
            log.info("size{}",size);
            itemList = itemService.findByAllItem(page, size);
            log.info("아이템 리스트 adminFindAllItem{}",itemList.toString());
            return ResponseEntity.ok(itemList);
        }

}
