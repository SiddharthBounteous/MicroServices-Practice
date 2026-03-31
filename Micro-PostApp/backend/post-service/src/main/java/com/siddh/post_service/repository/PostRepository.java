package com.siddh.post_service.repository;

import com.siddh.post_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    //feed sorted by recency
    List<Post> findAllByOrderByCreatedAtDesc();

    //hash-tag based search
    @Query("select p from Post p "+
            "join PostHashtag ph ON p.id=ph.postId "+
            "join Hashtag h ON ph.hashtagId=h.id "+
            "where h.tagName = :tagName "+
            "order BY p.createdAt desc")
    List<Post> findByHashtagName(@Param("tagName") String tagName);

    //fetch all posts for specific profile
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
}
