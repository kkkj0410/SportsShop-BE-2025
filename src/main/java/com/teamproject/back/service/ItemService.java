package com.teamproject.back.service;

import com.teamproject.back.dto.ItemDTO;
import com.teamproject.back.dto.ItemDetailImageDTO;
import com.teamproject.back.dto.ItemFormRequestDto;
import com.teamproject.back.dto.ItemFormResponseDto;
import com.teamproject.back.entity.Item;
import com.teamproject.back.entity.ItemDetailImage;
import com.teamproject.back.repository.CommentRepository;
import com.teamproject.back.repository.ItemRepository;
import com.teamproject.back.util.GcsImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;


    private final GcsImage gcsImage;

    @Autowired
    public ItemService(ItemRepository itemRepository, CommentRepository commentRepository, GcsImage gcsImage) {
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
        this.gcsImage = gcsImage;
    }

    public List<ItemFormResponseDto> findItemListByNew(int size, int page, String search){
        List<Item> itemList = itemRepository.findItemsWithPagination(size, page, search);
        if(itemList == null){
            log.info("상품 조회 실패");
            return null;
        }

        return itemListToItemFormResponseDtoList(itemList);
    }

    public List<ItemFormResponseDto> findItemListByPriceDesc(int size, int page, String search){
        List<Item> itemList = itemRepository.findItemsSortedByPriceDesc(size, page, search);
        if(itemList == null){
            log.info("상품 조회 실패");
            return null;
        }

        return itemListToItemFormResponseDtoList(itemList);
    }

    public List<ItemFormResponseDto> findItemListByPriceAsc(int size, int page, String search){
        List<Item> itemList = itemRepository.findItemsSortedByPriceAsc(size, page, search);
        if(itemList == null){
            log.info("상품 조회 실패");
            return null;
        }

        return itemListToItemFormResponseDtoList(itemList);
    }

    public List<ItemFormResponseDto> findItemsSortedByRecommendDesc(int size, int page, String search){
        List<Item> itemList = itemRepository.findItemsSortedByRecommendDesc(size, page, search);
        if(itemList == null){
            log.info("상품 조회 실패");
            return null;
        }

        return itemListToItemFormResponseDtoList(itemList);
    }

    public List<ItemFormResponseDto> findItemsSortedByComment(int size, int page, String search){
        List<Item> itemList = itemRepository.findItemsSortedByComment(size, page, search);
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
        Double averageRating = item.getAverageRating();
        if(averageRating == null){
            averageRating = commentRepository.findAverageRating(item.getId());
            if(averageRating == null){
                log.info("평점 평균 집계 실패");
            }
        }

        List<ItemDetailImageDTO> itemDetailImageDTOs = (item.getItemDetailImages() == null || item.getItemDetailImages().isEmpty())
                ? Collections.emptyList()  // null 또는 빈 리스트일 경우 빈 리스트 반환
                : item.getItemDetailImages().stream()
                .map(this::itemDetailImageToDTO)
                .collect(Collectors.toList());

        if(item != null){
            return ItemFormResponseDto.builder()
                    .id(item.getId())
                    .itemName(item.getItemName())
                    .itemDesc(item.getItemDesc())
                    .itemImg(item.getItemImg())
                    .itemDetailImages(itemDetailImageDTOs)
                    .averageRating(averageRating)
                    .commentCount(item.getCommentCount())
                    .itemStock(item.getItemStock())
                    .itemOriginPrice(item.getItemOriginPrice())
                    .itemPrice(item.getItemPrice())
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

    public ItemDetailImageDTO itemDetailImageToDTO(ItemDetailImage itemDetailImage){
        return ItemDetailImageDTO.builder()
                .imageIndex(itemDetailImage.getImgIndex())
                .imageUrl(itemDetailImage.getImg())
                .build();
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
                    .itemPrice(itemDto.getItemPrice())
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
                .itemPrice(itemDto.getItemPrice())
                .itemBrand(itemDto.getItemBrand())
                .category(itemDto.getCategory())
                .build();
    }


    public List<ItemDTO> searchItemList(int page, int size, String itemName) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<Item> itemList = itemRepository.searchItemList(page,size,itemName);

        return itemDTOList;
    }

    public List<ItemDTO> findByAllItem(int page, int size) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<Item> itemList = itemRepository.findByAllItem(page,size);
        int totalCount = itemRepository.itemCount();
        for (Item item : itemList) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setItemName(item.getItemName());
            itemDTO.setItemDesc(item.getItemDesc());
            itemDTO.setItemImg(item.getItemImg());
            itemDTO.setItemStock(item.getItemStock());
            itemDTO.setItemOriginPrice(item.getItemOriginPrice());
            itemDTO.setItemBrand(item.getItemBrand());
            itemDTO.setItemPrice(item.getItemPrice());
            itemDTO.setTotalData(totalCount);
            itemDTOList.add(itemDTO);
        }
        return itemDTOList;
    }

    public List<ItemDTO> findByItemName(int page, int size, String itemName) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<Item> itemList = itemRepository.findByItemName(page,size,itemName);
        int totalCount = itemRepository.itemCount();
        for (Item item : itemList) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setItemName(item.getItemName());
            itemDTO.setItemDesc(item.getItemDesc());
            itemDTO.setItemImg(item.getItemImg());
            itemDTO.setItemStock(item.getItemStock());
            itemDTO.setItemOriginPrice(item.getItemOriginPrice());
            itemDTO.setItemBrand(item.getItemBrand());
            itemDTO.setItemPrice(item.getItemPrice());
            itemDTO.setTotalData(totalCount);
            itemDTOList.add(itemDTO);
        }
        return itemDTOList;
    }

    public ItemDTO findByItemId(int id) {
        Item item = itemRepository.findByItemId(id);
        if(item != null){
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setItemName(item.getItemName());
            itemDTO.setItemDesc(item.getItemDesc());
            itemDTO.setItemImg(item.getItemImg());
            itemDTO.setItemStock(item.getItemStock());
            itemDTO.setItemOriginPrice(item.getItemOriginPrice());
            itemDTO.setItemBrand(item.getItemBrand());
            itemDTO.setItemPrice(item.getItemPrice());
            return itemDTO;
        }
        return null;
    }

//    public List<ItemDTO> findByItemName(String debouncedSearch) {
//        List<Item> itemList = itemRepository.findByItemName(debouncedSearch);
//        List<ItemDTO> itemDTOList = new ArrayList<>();
//        for (Item item : itemList) {
//            ItemDTO itemDTO = new ItemDTO();
//            itemDTO.setId(item.getId());
//            itemDTO.setItemName(item.getItemName());
//            itemDTO.setItemDesc(item.getItemDesc());
//            itemDTO.setItemImg(item.getItemImg());
//            itemDTO.setItemStock(item.getItemStock());
//            itemDTO.setItemOriginPrice(item.getItemOriginPrice());
//            itemDTO.setItemBrand(item.getItemBrand());
//            itemDTO.setItemPrice(item.getItemPrice());
//            itemDTOList.add(itemDTO);
//        }
//        return itemDTOList;
//    }


    public List<ItemFormResponseDto> findByItemName(String debouncedSearch) {
        List<Item> itemList = itemRepository.findByItemName(debouncedSearch);
        if(itemList.isEmpty()){
            return new ArrayList<>();
        }

        return itemListToItemFormResponseDtoList(itemList);
    }
}
