package com.teamproject.back.controller;

import com.teamproject.back.dto.ItemDTO;
import com.teamproject.back.dto.ItemFormResponseDto;
import com.teamproject.back.dto.UserDto;
import com.teamproject.back.entity.Item;
import com.teamproject.back.service.ItemService;
import com.teamproject.back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final UserService userService;
    private final ItemService itemService;

    @GetMapping("/api/home")
    public ResponseEntity<String>homeController(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("name:{}",username);
        log.info("Authoriztion:{}",SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        String roel = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        return ResponseEntity.ok("username: " + username);
    }
    @GetMapping("/api/admin/home")
    public ResponseEntity<UserDto>adminHomeController(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.findByUser(username);
        return ResponseEntity.ok(userDto); // 로그인 성공
    }
    @GetMapping("/api/header")
    public ResponseEntity<String> getHeaderController(){
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        return ResponseEntity.ok(role);
    }
//    @GetMapping("/api/admin/item/find")
//    public ResponseEntity<List<ItemDTO>> adminItemFindController(){
//        List<ItemDTO> items = itemService.findAllItem();
//        return Res
//        ponseEntity.ok(items); //전체 상품이니까
//    }

    //(3.3) - 리뷰, 평점 추가 조회
    @GetMapping("/api/search/{debouncedSearch}")
    public ResponseEntity<List<ItemFormResponseDto>> searchController(@PathVariable String debouncedSearch){
        List<ItemFormResponseDto> itemFormResponseDtoList = itemService.findByItemName(debouncedSearch);

        return ResponseEntity.ok(itemFormResponseDtoList);
    }
}
