package com.teamproject.back.service;


import com.teamproject.back.dto.CommentDto;
import com.teamproject.back.dto.ItemFormResponseDto;
import com.teamproject.back.entity.Comment;
import com.teamproject.back.entity.Item;
import com.teamproject.back.entity.Users;
import com.teamproject.back.repository.CommentRepository;
import com.teamproject.back.repository.LikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, LikeRepository likeRepository) {
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }


    public CommentDto findComment(Long id){
        return toCommentDto(commentRepository.findById(id));
    }


    public List<CommentDto> findParentComments(){
        return toCommentDtoList(commentRepository.findParentComments());
    }

    public List<CommentDto> findParentCommentsByItemId(Integer itemId){
        List<Comment> comments = commentRepository.findParentCommentsByItemId(itemId);
        log.info("comments : {}", comments);
        return toCommentDtoList(comments);
    }

    public List<CommentDto> findChildCommentsById(Long id){
        return toCommentDtoList(commentRepository.findChildCommentsByIdWithUsers(id));
    }

    public CommentDto createComment(Integer itemId, CommentDto commentDto){
        commentRepository.save(itemId, toComment(commentDto));
        return commentDto;
    }

    public CommentDto createReply(Long parentCommentId, CommentDto childCommentDto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        commentRepository.saveReply(parentCommentId, email,toComment(childCommentDto));

        return childCommentDto;
    }

    public CommentDto update(CommentDto commentDto){
        commentRepository.update(toComment(commentDto));
        return commentDto;
    }

    //삭제 : 자식 좋아요 -> 자식 댓글 -> 본인 좋아요 -> 본인 댓글
    public int delete(Long id){
        List<Comment> commentList = commentRepository.findChildCommentById(id);

        commentList.forEach(comment -> {
            likeRepository.deleteAllByCommentId(comment.getId());
            commentRepository.delete(comment.getId());

        });

        likeRepository.deleteAllByCommentId(id);
        commentRepository.delete(id);

        return 1;
    }



    private Comment toComment(CommentDto commentDto){
        return Comment.builder()
                .id(commentDto.getId())
                .content(commentDto.getContent())
                .rating(commentDto.getRating())
//                .likes(commentDto.getLikes())
                .created_date(commentDto.getCreated_date())
//                .users(commentDto.getUsers())
//                .item(commentDto.getItem())
//                .parentComment(commentDto.getParentComment())
//                .childComments(commentDto.getChildComments())
                .build();
    }

    private CommentDto toCommentDto(Comment comment){
        if(comment.getParentComment() == null){
            return CommentDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .rating(comment.getRating())
//                    .like(comment.getLikes())
                    .usersId(comment.getUsers().getId())
                    .email(comment.getUsers().getEmail())
                    .itemId(comment.getItem().getId())
                    .created_date(comment.getCreated_date())
                    .build();
        }

        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .rating(comment.getRating())
//                .like(comment.getLikes())
                .usersId(comment.getUsers().getId())
                .email(comment.getUsers().getEmail())
                .itemId(comment.getItem().getId())
                .parentCommentId(comment.getParentComment().getId())
                .created_date(comment.getCreated_date())
//                .parentComment(comment.getParentComment())
//                .childComments(comment.getChildComments())
                .build();
    }


    private List<CommentDto> toCommentDtoList(List<Comment> comments){
        return comments.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());
    }

    public Map<String,Object> findByRatingAndCommentCount(long id) {
        Map<String, Object> map = commentRepository.findByRatingAndCommentCount((Long)id);
        return map;
    }
}
