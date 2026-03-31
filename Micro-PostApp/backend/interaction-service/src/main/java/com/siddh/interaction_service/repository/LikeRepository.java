package com.siddh.interaction_service.repository;

import com.siddh.interaction_service.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    //total no of likes on a post
    int countByPostId(Long postId);

    //check for already liked post
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    //unlike the post
    void deleteByPostIdAndUserId(Long postId, Long userId);
}
