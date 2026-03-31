package com.siddh.interaction_service.controller;

import com.siddh.interaction_service.dto.CommentRequestDTO;
import com.siddh.interaction_service.entity.Comment;
import com.siddh.interaction_service.repository.CommentRepository;
import com.siddh.interaction_service.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    //get the total likes for the post
    @GetMapping("/{postId}/likes/count")
    public int getLikeCount(@PathVariable("postId") Long postId) {
        return likeRepository.countByPostId(postId);
    }

    //get the total comments for the post
    @GetMapping("/{postId}/comments/count")
    public int getCommentCount(@PathVariable("postId") Long postId){
        return commentRepository.countByPostId(postId);
    }

    //checking if particular user already liked the post
    @GetMapping("/{postId}/likes/check")
    public boolean checkUserLiked(@PathVariable("postId") Long postId, @RequestParam("userId") Long userId){
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> toggleLike(@PathVariable("postId") Long postId,@RequestHeader("X-User-Id") Long userId){

        interactionService.toggleLike(postId, userId);
        return ResponseEntity.ok().build();
    }

    //submitting comment
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable("postId") Long postId,@RequestHeader("X-User-Id") Long userId, @RequestBody CommentRequestDTO requestDTO) {

        Comment savedComment = interactionService.addComment(postId, userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }
}
