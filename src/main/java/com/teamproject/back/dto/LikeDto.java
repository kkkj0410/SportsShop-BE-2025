package com.teamproject.back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeDto {

    private Long id;
    private Long commentId;
    private Long userId;
    private Long totalLike;
    private boolean clicked;
}
