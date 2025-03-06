package com.teamproject.back.controller;

import com.teamproject.back.dto.CommentDto;
import com.teamproject.back.service.CommentImageService;
import com.teamproject.back.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final CommentImageService commentImageService;

    @Autowired
    public CommentController(CommentService commentService, CommentImageService commentImageService) {
        this.commentService = commentService;
        this.commentImageService = commentImageService;
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id){
        CommentDto commentDto = commentService.findComment(id);
        if(commentDto == null){
            return ResponseEntity.badRequest().body("댓글 조회에 실패했습니다");
        }

        return ResponseEntity.ok(commentDto);
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getComments(){
        List<CommentDto> commentDto = commentService.findParentComments();
//        List<CommentDto> commentDto = commentService.findComments();
        if(commentDto == null){
            return ResponseEntity.badRequest().body("댓글 조회에 실패했습니다");
        }

        return ResponseEntity.ok(commentDto);
    }

    @GetMapping("/item/{itemId}/comments")
    public ResponseEntity<?> getCommentsByItemId(@PathVariable Integer itemId){
//        List<CommentDto> commentDto = commentService.findParentCommentsByItemId(itemId);
        List<CommentDto> commentDto = commentService.findCommentsByItemId(itemId);

        if(commentDto == null){
            return ResponseEntity.badRequest().body("댓글 조회에 실패했습니다");
        }

        return ResponseEntity.ok(commentDto);
    }

    @GetMapping("/comment/{id}/replies")
    public ResponseEntity<?> getChildCommentsById(@PathVariable Long id){
        List<CommentDto> commentDto = commentService.findChildCommentsById(id);
        if(commentDto == null){
            return ResponseEntity.badRequest().body("댓글 조회에 실패했습니다");
        }

        log.info("commentDto : {}", commentDto);
        return ResponseEntity.ok(commentDto);
    }

    @PostMapping("/item/{itemId}/comment")
    public ResponseEntity<?> postCommentByItemId(
            @PathVariable Integer itemId,
            @ModelAttribute CommentDto commentDto,
            @RequestPart(value = "commentFiles", required = false) List<MultipartFile> commentFiles){
        CommentDto saveCommentDto = commentService.createComment(itemId, commentDto);

        if(saveCommentDto == null){
            return ResponseEntity.badRequest().body("댓글 저장에 실패했습니다");
        }

        if(commentFiles != null){
            List<MultipartFile> files = commentImageService.saveAll(saveCommentDto.getId(), commentFiles);
            if(files == null){
                return ResponseEntity.badRequest().body("사진 저장에 실패했습니다");
            }
        }

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/comment/{id}")
    public ResponseEntity<?> postChildCommentById(@PathVariable Long id,@RequestBody CommentDto commentDto){
        CommentDto saveCommentDto = commentService.createReply(id,commentDto);
        if(saveCommentDto == null){
            return ResponseEntity.badRequest().body("댓글 저장에 실패했습니다");
        }

        return ResponseEntity.ok("ok");
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> patchComment(@RequestBody CommentDto commentDto){
        CommentDto updateCommentDto = commentService.update(commentDto);
        if(updateCommentDto == null){
            return ResponseEntity.badRequest().body("댓글 수정에 실패했습니다");
        }

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> patchComment(@PathVariable Long id){
        if(commentService.delete(id) == 0){
            return ResponseEntity.badRequest().body("댓글 삭제에 실패했습니다");
        }

        return ResponseEntity.ok("ok");
    }

}
