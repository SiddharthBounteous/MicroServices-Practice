package com.siddh.interaction_service.service;

import com.siddh.interaction_service.dto.CommentRequestDTO;
import com.siddh.interaction_service.entity.Comment;
import com.siddh.interaction_service.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public int getCommentCount(Long postId){
        return commentRepository.countByPostId(postId);
    }

    public Comment addComment(Long postId, Long userId, CommentRequestDTO requestDTO){
        Comment newComment=Comment.builder()
                .postId(postId)
                .userId(userId)
                .content(requestDTO.getContent())
                .build();

        return commentRepository.save(newComment);
    }
}
