package com.teamproject.back.repository;

import com.teamproject.back.entity.Comment;
import com.teamproject.back.entity.CommentImage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class CommentImageRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public CommentImage save(Long commentId, CommentImage commentImage){
        Comment comment = em.find(Comment.class, commentId);

        commentImage.fetchComment(comment);
        em.persist(commentImage);
        em.flush();

        return commentImage;
    }

    @Transactional
    public List<CommentImage> saveAll(Long commentId, List<CommentImage> commentImages){

        Comment comment = em.find(Comment.class, commentId);

        log.info("commentImages : {}", commentImages);

        commentImages.forEach((commentImage) -> {
            commentImage.fetchComment(comment);
            em.persist(commentImage);
        });
        em.flush();

        return commentImages;
    }

}
