package com.teamproject.back.controller;

import com.teamproject.back.dto.ItemFormResponseDto;
import com.teamproject.back.dto.LikeDto;
import com.teamproject.back.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/comment")
@Slf4j
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/{commentId}/like")
    public ResponseEntity<?> getLikes(@PathVariable("commentId") Long commentId){
        LikeDto likeDto = likeService.countLike(commentId);
        if(likeDto != null){
            log.info("likeDto : {}", likeDto);
            return ResponseEntity.ok(likeDto);
        }

        return ResponseEntity.badRequest().body("like 조회 실패");
    }

    @PostMapping("/like")
    public ResponseEntity<?> postLike(@RequestParam Long commentId){
        if(likeService.addLike(commentId) != null){
            log.info("commentId : {}", commentId);
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.badRequest().body("like 저장 실패");
    }

    @DeleteMapping("/like")
    public ResponseEntity<?> deleteLike(@RequestParam Long commentId){
        if(likeService.removeLike(commentId) == 1){
            log.info("delete like : {}", commentId);
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.badRequest().body("like 삭제 실패");
    }


}
