package com.teamproject.back.dto;


import com.teamproject.back.entity.Comment;
import com.teamproject.back.entity.Item;
import com.teamproject.back.entity.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentDto {
    private Long id;

    private String content;

    private Integer rating;

    private LocalDateTime created_date;

    private Long usersId;

    private String email;

    private Integer itemId;

    private Long parentCommentId;

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
