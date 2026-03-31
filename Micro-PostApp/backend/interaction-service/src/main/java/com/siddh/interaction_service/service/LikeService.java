package com.siddh.interaction_service.service;

import com.siddh.interaction_service.entity.Like;
import com.siddh.interaction_service.repository.LikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    public int getLikeCount(Long postId){
        return likeRepository.countByPostId(postId);
    }

    public boolean checkUserLiked(Long postId, Long userId){
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }

    @Transactional
    public void toggleLike(Long postId, Long userId){
        if(likeRepository.existsByPostIdAndUserId(postId, userId)){
            likeRepository.deleteByPostIdAndUserId(postId, userId);
        }
        else {
            Like newLike = Like.builder()
                    .postId(postId)
                    .userId(userId)
                    .build();
            likeRepository.save(newLike);
        }
    }
}
