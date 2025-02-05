package com.teamproject.back.controller;

import com.teamproject.back.dto.CommentDto;
import com.teamproject.back.dto.ItemDTO;
import com.teamproject.back.dto.UserDto;
import com.teamproject.back.entity.Item;
import com.teamproject.back.service.CommentService;
import com.teamproject.back.service.ItemService;
import com.teamproject.back.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final ItemService itemService;
    private final UserService userService;
    private final CommentService commentService;

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
            itemList = itemService.findByAllItem(page, size);
            return ResponseEntity.ok(itemList);
        }
        @GetMapping("/api/admin/users/findAll")
        public ResponseEntity<Map<String,Object>> adminFindAllUser (
                @RequestParam int page,
                @RequestParam int size
        ){
        Map<String,Object> map = new HashMap<>();
        List<UserDto> userList = userService.findAllUsersList(page,size);
        int userCounter = userService.userCount();
        map.put("userData",userList);
        map.put("count",userCounter);
        return ResponseEntity.ok(map);
        }
        @GetMapping("/api/admin/userinfo/{id}")
        public ResponseEntity<UserDto> adminFindUserInfo (@PathVariable Long id){
            UserDto userDto = userService.findByUserId(id);
            return ResponseEntity.ok(userDto);
        }
        @GetMapping("/api/admin/item/detail/{id}")
        public ResponseEntity<Map<String,Object>> adminFindItemDetail (@PathVariable int id){
            Map<String,Object> map = new HashMap<>();
            log.info("아이템 id{}",id);
            ItemDTO itemDTO  = itemService.findByItemId(id);
            Map<String,Object> result = commentService.findByRatingAndCommentCount((long) id);
            map.put("itemData",itemDTO);
            map.put("commentCount",result.get("commentCount"));
            map.put("ratingAvg",result.get("ratingAvg"));
            return ResponseEntity.ok(map);
        }


}
