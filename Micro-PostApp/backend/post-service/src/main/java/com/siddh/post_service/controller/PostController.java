package com.siddh.post_service.controller;

import com.siddh.post_service.dto.PostRequestDTO;
import com.siddh.post_service.dto.PostResponseDTO;
import com.siddh.post_service.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody PostRequestDTO requestDTO){
        PostResponseDTO response=postService.createPost(userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponseDTO>> getFeed(@RequestHeader("X-User-Id") Long userId){
        List<PostResponseDTO> feed=postService.getFeed(userId);
        return ResponseEntity.ok(feed);
    }
}
