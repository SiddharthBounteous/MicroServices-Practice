package com.siddh.post_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "interaction-service")
public interface InteractionClient {
    @GetMapping("/api/v1/interactions/{postId}/likes/count")
    int getLikeCount(@PathVariable("postId") Long postId);

    @GetMapping("/api/v1/interactions/{postId}/comments/count")
    int getCommentCount(@PathVariable("postId") Long postId);

    @GetMapping("/api/v1/interactions/{postId}/likes/check")
    boolean checkUserLiked(@PathVariable("postId") Long postId, @RequestParam("userId") Long userId);
}
