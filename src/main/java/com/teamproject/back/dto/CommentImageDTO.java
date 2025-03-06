package com.teamproject.back.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class CommentImageDTO {

    private Integer imageIndex;

    private MultipartFile imageFile;

    private String imageUrl;
}
