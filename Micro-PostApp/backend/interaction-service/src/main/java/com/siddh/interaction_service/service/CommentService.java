package com.siddh.interaction_service.service;

import com.siddh.interaction_service.client.AuthClient;
import com.siddh.interaction_service.dto.CommentRequestDTO;
import com.siddh.interaction_service.dto.CommentResponseDTO;
import com.siddh.interaction_service.dto.UserSummaryDTO;
import com.siddh.interaction_service.entity.Comment;
import com.siddh.interaction_service.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final AuthClient authClient;

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

    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);

        return comments.stream()
                .map(comment -> {
                            String username = "User";
                            try {
                                UserSummaryDTO userSummary = authClient.getUserSummary(comment.getUserId());
                                username = userSummary.getUsername();
                            } catch (Exception e) {
                                System.out.println("Failed to fetch username for userId=" + comment.getUserId());
                            }
                            return CommentResponseDTO.builder()
                                    .id(comment.getId())
                                    .postId(comment.getPostId())
                                    .userId(comment.getUserId())
                                    .username(username)
                                    .content(comment.getContent())
                                    .createdAt(comment.getCreatedAt())
                                    .build();
                        }
                ).toList();
    }
    }


