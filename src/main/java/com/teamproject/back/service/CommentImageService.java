package com.teamproject.back.service;


import com.teamproject.back.entity.CommentImage;
import com.teamproject.back.entity.ItemDetailImage;
import com.teamproject.back.repository.CommentImageRepository;
import com.teamproject.back.util.GcsImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CommentImageService {

    private final CommentImageRepository commentImageRepository;
    private final GcsImage gcsImage;

    @Autowired
    public CommentImageService(CommentImageRepository commentImageRepository, GcsImage gcsImage) {
        this.commentImageRepository = commentImageRepository;
        this.gcsImage = gcsImage;
    }


    public List<MultipartFile> saveAll(Long commentId, List<MultipartFile> files){
        List<CommentImage> commentImages = new ArrayList<>();

        int count = 1;
        for(MultipartFile file : files){
            String imgUrl = gcsImage.uploadImage(file);
            if(imgUrl == null){
                log.info("이미지 업로드 실패");
                return null;
            }


            commentImages.add(toEntity(count++, imgUrl));
        }

        commentImageRepository.saveAll(commentId, commentImages);

        return files;
    }

    private CommentImage toEntity(int count, String imgUrl) {
        return CommentImage.builder()
                .imgIndex(count)
                .img(imgUrl)
                .build();
    }


}
