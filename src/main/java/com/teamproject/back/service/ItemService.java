package com.teamproject.back.service;

import com.teamproject.back.dto.ItemDTO;
import com.teamproject.back.entity.Item;
import com.teamproject.back.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    public List<ItemDTO> findAllItem() {
        List<Item> items = itemRepository.findAllItem();
        List<ItemDTO> itemsDTO = new ArrayList<>();
        for (Item item : items) {
            itemEntityToiItemDTO(item);
        }
        return itemsDTO;
    }
    public ItemDTO itemEntityToiItemDTO(Item item){
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setItemName(item.getItemName());
        itemDTO.setItemDesc(item.getItemDesc());
        itemDTO.setItemImg(item.getItemImg());
        itemDTO.setItemRating(item.getItemRating());
        itemDTO.setItemStock(item.getItemStock());
        itemDTO.setItemPrice(item.getItemPrice());
        itemDTO.setItemOriginPrice(item.getItemOriginPrice());
        itemDTO.setItemBrand(itemDTO.getItemBrand());
        return itemDTO;
    }
    public ItemDTO itemDTOToItemEntity(Item item){
        return null; // 수정 및 변경해야함
    }
}
