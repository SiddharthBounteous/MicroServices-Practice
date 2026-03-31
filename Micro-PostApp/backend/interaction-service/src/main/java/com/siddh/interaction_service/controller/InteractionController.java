package com.siddh.interaction_service.controller;

import com.siddh.interaction_service.dto.CommentRequestDTO;
import com.siddh.interaction_service.entity.Comment;
import com.siddh.interaction_service.repository.CommentRepository;
import com.siddh.interaction_service.repository.LikeRepository;
import com.siddh.interaction_service.service.CommentService;
import com.siddh.interaction_service.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final LikeService likeService;
    private final CommentService commentService;

    //get the total likes for the post
    @GetMapping("/{postId}/likes/count")
    public int getLikeCount(@PathVariable("postId") Long postId) {
        return likeService.getLikeCount(postId);
    }

    //get the total comments for the post
    @GetMapping("/{postId}/comments/count")
    public int getCommentCount(@PathVariable("postId") Long postId){
        return commentService.getCommentCount(postId);
    }

    //checking if particular user already liked the post
    @GetMapping("/{postId}/likes/check")
    public boolean checkUserLiked(@PathVariable("postId") Long postId, @RequestParam("userId") Long userId){
        return likeService.checkUserLiked(postId, userId);
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> toggleLike(@PathVariable("postId") Long postId,@RequestHeader("X-User-Id") Long userId){
        likeService.toggleLike(postId, userId);
        return ResponseEntity.ok().build();
    }

    //submitting comment
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable("postId") Long postId,@RequestHeader("X-User-Id") Long userId, @RequestBody CommentRequestDTO requestDTO){
        Comment savedComment=commentService.addComment(postId, userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }
}
