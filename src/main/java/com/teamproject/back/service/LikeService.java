package com.teamproject.back.service;


import com.teamproject.back.dto.LikeDto;
import com.teamproject.back.entity.Likes;
import com.teamproject.back.repository.LikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public LikeDto countLike(Long commentId){
        Long totalLike = likeRepository.findCountByCommentId(commentId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean clicked = likeRepository.checkClicked(commentId, email);

        return LikeDto.builder()
                .totalLike(totalLike)
                .clicked(clicked)
                .build();
    }

    public Long addLike(Long commentId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(likeRepository.save(commentId, email) == null){
            log.info("like 저장 실패");
            return null;
        }
        log.info("commentId : {}", commentId);
        return commentId;
    }

    public int removeLike(Long commentId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(likeRepository.delete(commentId, email) == 0){
            log.info("like 삭제 실패");
            return 0;
        }

        log.info("like 삭제 성공");
        return 1;
    }


//    private LikeDto toLikeDto(Likes like){
//        return LikeDto.builder()
//                .id(like.getId())
//                .commentId(like.getComment().getId())
//                .userId(like.getUsers().getId())
//                .build();
//    }
}
