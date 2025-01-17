package com.teamproject.back.service;
import com.teamproject.back.dto.ItemFormResponseDto;
import com.teamproject.back.dto.ItemFormRequestDto;
import com.teamproject.back.entity.Category;
import com.teamproject.back.entity.Item;
import com.teamproject.back.repository.ItemRepository;
import com.teamproject.back.util.GcsImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final GcsImage gcsImage;

    @Autowired
    public ItemService(ItemRepository itemRepository, GcsImage gcsImage) {
        this.itemRepository = itemRepository;
        this.gcsImage = gcsImage;
    }

    public List<ItemFormResponseDto> findItemList(int size, int page){
        List<Item> itemList = itemRepository.findItemsWithPagination(size, page);
        if(itemList == null){
            log.info("상품 조회 실패");
            return null;
        }

        return itemListToItemFormResponseDtoList(itemList);
    }

    public ItemFormResponseDto save(ItemFormRequestDto itemFormRequestDto){
        String imgUrl = gcsImage.uploadImage(itemFormRequestDto.getImageFile());
        if(imgUrl == null){
            log.info("이미지 업로드 실패");
            return null;
        }

        itemFormRequestDto.setItemImg(imgUrl);
        Item saveItem = itemRepository.save(itemFormRequestDtoToItem(itemFormRequestDto));
        if(saveItem == null){
            log.info("상품 저장 실패");
            return null;
        }
        return itemToItemFormResponseDto(saveItem);
    }

    public ItemFormResponseDto findById(int id){
        return itemToItemFormResponseDto(itemRepository.findById(id));
    }

    public int deleteById(int id, String imageUrl){
        if(!gcsImage.deleteImage(imageUrl)){
            log.info("이미지 삭제 실패");
        }

        if(itemRepository.deleteById(id) == 0){
            log.info("회원 삭제 실패");
        }
        return 1;
    }

    public ItemFormResponseDto updateItem(ItemFormRequestDto itemFormRequestDto){
        if(gcsImage.overWriteImage(itemFormRequestDto.getImageFile(), itemFormRequestDto.getItemImg()) == null){
            log.info("이미지 수정 실패");
            return null;
        }

        Item updateItem = itemRepository.updateItem(itemFormRequestDtoToItem(itemFormRequestDto));
        if(updateItem == null){
            return null;
        }

        return itemToItemFormResponseDto(updateItem);
    }
//
//    private int deleteImage(int id){
//        ItemFormResponseDto itemFormResponseDto = findById(id);
//        if(itemFormResponseDto == null){
//            log.info("회원 조회 실패");
//            return 0;
//        }
//
//        if(!gcsImage.deleteImage(itemFormResponseDto.getItemImg())){
//            log.info("이미지 삭제 실패");
//            return 0;
//        }
//
//        return 1;
//    }


    private ItemFormResponseDto itemToItemFormResponseDto(Item item){
        if(item != null){
            return ItemFormResponseDto.builder()
                    .id(item.getId())
                    .itemName(item.getItemName())
                    .itemDesc(item.getItemDesc())
                    .itemImg(item.getItemImg())
                    .itemRating(item.getItemRating())
                    .itemStock(item.getItemStock())
                    .itemOriginPrice(item.getItemOriginPrice())
                    .itemBrand(item.getItemBrand())
                    .category(item.getCategory())
                    .build();
        }
        return null;
    }

    private List<ItemFormResponseDto> itemListToItemFormResponseDtoList(List<Item> items){
        return items.stream()
                .map(this::itemToItemFormResponseDto)
                .collect(Collectors.toList());
    }



//    private Item itemFormReponseDtoToItem(ItemFormResponseDto itemFormResponseDto){
//        if(itemFormResponseDto != null){
//            return Item.builder()
//                    .id(itemFormResponseDto.getId())
//                    .itemName(itemFormResponseDto.getItemName())
//                    .itemDesc(itemFormResponseDto.getItemDesc())
//                    .itemImg(itemFormResponseDto.getItemImg())
//                    .itemStock(itemFormResponseDto.getItemStock())
//                    .itemOriginPrice(itemFormResponseDto.getItemOriginPrice())
//                    .itemBrand(itemFormResponseDto.getItemBrand())
//                    .category(itemFormResponseDto.getCategory())
//                    .build();
//        }
//        return null;
//    }

    private Item itemFormRequestDtoToItem(ItemFormRequestDto itemDto){
        if(itemDto.getId() != null){
            return Item.builder()
                    .id(itemDto.getId())
                    .itemName(itemDto.getItemName())
                    .itemDesc(itemDto.getItemDesc())
                    .itemImg(itemDto.getItemImg())
                    .itemStock(itemDto.getItemStock())
                    .itemOriginPrice(itemDto.getItemOriginPrice())
                    .itemBrand(itemDto.getItemBrand())
                    .category(itemDto.getCategory())
                    .build();
        }


        return Item.builder()
                .itemName(itemDto.getItemName())
                .itemDesc(itemDto.getItemDesc())
                .itemImg(itemDto.getItemImg())
                .itemStock(itemDto.getItemStock())
                .itemOriginPrice(itemDto.getItemOriginPrice())
                .itemBrand(itemDto.getItemBrand())
                .category(itemDto.getCategory())
                .build();
    }


}
