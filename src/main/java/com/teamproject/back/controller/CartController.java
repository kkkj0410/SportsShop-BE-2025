package com.teamproject.back.controller;

import com.teamproject.back.dto.CartDTO;
import com.teamproject.back.entity.Cart;
import com.teamproject.back.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api")
public class CartController{

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/items")
    public ResponseEntity<?> getCart(
            @RequestParam("size") int size,
            @RequestParam("page") int page
    ){
        List<CartDTO> cartDtos = cartService.findCartAll(size,page);

        if(cartDtos == null){
            return ResponseEntity.badRequest().body("장바구니 조회에 실패 했습니다.");
        }

        return ResponseEntity.ok(cartDtos);
    }

    @GetMapping("/cart/item/{itemId}")
    public ResponseEntity<?> getCartByItemId(@PathVariable Integer itemId){
        CartDTO cartDTO = cartService.findCartByItemId(itemId);

        if(cartDTO == null){
            return ResponseEntity.badRequest().body("장바구니 조회에 실패했습니다.");
        }
        return ResponseEntity.ok(cartDTO);
    }


    @PostMapping("/cart/item")
    public ResponseEntity<?> postCart(@RequestBody CartDTO cartDTO){
        CartDTO savedCartDTO = cartService.save(cartDTO);

        if(savedCartDTO == null){
            return ResponseEntity.badRequest().body("장바구니 저장에 실패했습니다.");
        }

        return ResponseEntity.ok("ok");
    }

    @PatchMapping("/cart/item")
    public ResponseEntity<?> patchCart(@RequestBody CartDTO cartDTO){
        int result = cartService.updateCart(cartDTO);
        if(result != 1){
            return ResponseEntity.badRequest().body("장바구니 수정에 실패했습니다.");
        }
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<?> deleteCart(@PathVariable Long cartId){
        int result = cartService.deleteCart(cartId);
        if(result != 1){
            return ResponseEntity.badRequest().body("장바구니 수정에 실패했습니다.");
        }
        return ResponseEntity.ok("ok");
    }

}
