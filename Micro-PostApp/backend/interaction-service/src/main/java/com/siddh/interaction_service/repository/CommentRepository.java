package com.siddh.interaction_service.repository;

import com.siddh.interaction_service.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    //getting comments
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);

    //view no. of comments
    int countByPostId(Long postId);
}
