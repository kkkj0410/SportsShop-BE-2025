package com.teamproject.back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;


@Table
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer imgIndex;

    private String img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public void fetchComment(Comment comment){
        this.comment = comment;

        if (comment.getCommentImages() == null) {
            comment.setCommentImages(new ArrayList<>());
        }
        this.comment.getCommentImages().add(this);
    }

    @Override
    public String toString() {
        return "CommentImage{" +
                "id=" + id +
                ", imgIndex=" + imgIndex +
                ", img='" + img + '\'' +
                '}';
    }
}
