package com.teamproject.back.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentDto {
    private Long id;

    private String content;

    private Double rating;

    private LocalDateTime created_date;

    private Long usersId;

    private String username;

    private String email;

    private Integer itemId;

    private Long parentCommentId;

    private List<CommentImageDTO> commentImages;

//    private List<CommentDto> childComments;

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", created_date=" + created_date +
                '}';
    }
}
