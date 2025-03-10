package com.teamproject.back.service;


import com.teamproject.back.dto.CartDTO;
import com.teamproject.back.dto.CommentDto;
import com.teamproject.back.entity.Cart;
import com.teamproject.back.entity.Comment;
import com.teamproject.back.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartDTO save(CartDTO cartDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartRepository.save(toEntity(cartDTO), cartDTO.getItemId(), email);

        return toDTO(cart);
    }

    public CartDTO findCartByItemId(Integer itemId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartRepository.findCart(itemId, email);

        return toDTO(cart);
    }

    public List<CartDTO> findCartAll(int size, int page){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Cart> carts = cartRepository.findCartAll(email, size, page);
        return toDTOs(carts);
    }

    public int updateCart(CartDTO cartDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return cartRepository.updateCartQuantity(toEntity(cartDTO), cartDTO.getItemId(), email);
    }

    public int deleteCart(Long cartId){
        return cartRepository.deleteCart(cartId);
    }



    public List<CartDTO> toDTOs(List<Cart> carts){
        if(carts.isEmpty()){
            return null;
        }

        return carts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

    }

    public CartDTO toDTO(Cart cart){
        if(cart == null){
            return null;
        }

        return CartDTO.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .itemId(cart.getItem().getId())
                .itemName(cart.getItem().getItemName())
                .itemImg(cart.getItem().getItemImg())
                .itemPrice(cart.getItem().getItemPrice())
                .build();
    }

    public Cart toEntity(CartDTO cartDTO){
        if(cartDTO == null){
            return null;
        }
        return Cart.builder()
                .id(cartDTO.getId())
                .quantity(cartDTO.getQuantity())
                .build();
    }
}
