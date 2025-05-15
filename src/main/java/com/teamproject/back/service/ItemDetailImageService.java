package com.teamproject.back.service;

import com.teamproject.back.dto.ItemDetailImageDTO;
import com.teamproject.back.entity.ItemDetailImage;
import com.teamproject.back.repository.ItemDetailImageRepository;
import com.teamproject.back.util.GcsImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@Slf4j
public class ItemDetailImageService {

    private final ItemDetailImageRepository itemDetailImageRepository;
    private final GcsImage gcsImage;

    @Autowired
    public ItemDetailImageService(ItemDetailImageRepository itemDetailImageRepository, GcsImage gcsImage) {
        this.itemDetailImageRepository = itemDetailImageRepository;
        this.gcsImage = gcsImage;
    }

    public List<MultipartFile> saveAll(Integer itemId, List<MultipartFile> files){
        List<ItemDetailImage> itemDetailImages = new ArrayList<>();

        int count = 1;
        for(MultipartFile file : files){
            String imgUrl = gcsImage.uploadImage(file);
            if(imgUrl == null){
                log.info("이미지 업로드 실패");
                return null;
            }


            itemDetailImages.add(toEntity(count++, imgUrl));
        }

        itemDetailImageRepository.saveAll(itemId, itemDetailImages);

        return files;
    }



    public ItemDetailImageDTO toDTO(Integer imageIndex, MultipartFile file){
        return ItemDetailImageDTO.builder()
                .imageIndex(imageIndex)
                .imageFile(file)
                .build();
    }


    public ItemDetailImage toEntity(ItemDetailImageDTO itemDetailImageDTO, String imgUrl){
        return ItemDetailImage.builder()
                .imgIndex(itemDetailImageDTO.getImageIndex())
                .img(imgUrl)
                .build();
    }

    public ItemDetailImage toEntity(Integer imgIndex, String imgUrl){
        return ItemDetailImage.builder()
                .imgIndex(imgIndex)
                .img(imgUrl)
                .build();
    }


}
