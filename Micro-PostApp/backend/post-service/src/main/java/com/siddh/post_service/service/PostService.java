package com.siddh.post_service.service;

import com.siddh.post_service.client.AuthClient;
import com.siddh.post_service.client.InteractionClient;
import com.siddh.post_service.dto.PostRequestDTO;
import com.siddh.post_service.dto.PostResponseDTO;
import com.siddh.post_service.dto.UserSummaryDTO;
import com.siddh.post_service.entity.Post;
import com.siddh.post_service.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final InteractionClient interactionClient;
    private final AuthClient authClient;
    private final HttpServletRequest request;

    //creating a post
    public PostResponseDTO createPost(Long userId, PostRequestDTO postRequestDTO){
        Post post=Post.builder()
                .userId(userId)
                .content(postRequestDTO.getContent())
                .build();

        Post savedPost=postRepository.save(post);

        return mapToDTO(savedPost,userId);
    }

    //fetch the feed
    public List<PostResponseDTO> getFeed(Long currentUserId){
        List<Post> posts=postRepository.findAllByOrderByCreatedAtDesc();

        return posts.stream()
                .map(post -> mapToDTO(post, currentUserId))
                .collect(Collectors.toList());
    }

    private PostResponseDTO mapToDTO(Post post, Long currentUserId) {
        int likes= interactionClient.getLikeCount(post.getId());
        int comments= interactionClient.getCommentCount(post.getId());
        boolean hasLiked=interactionClient.checkUserLiked(post.getId(), currentUserId);
        String authHeader= request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);
        UserSummaryDTO userSummaryDTO= authClient.getUserSummary(post.getUserId(),authHeader);

        return PostResponseDTO.builder()
                .postId(post.getId())
                .userId(post.getUserId())
                .username(userSummaryDTO.getUsername())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .likeCount(likes)
                .commentCount(comments)
                .hasLiked(hasLiked)
                .build();
    }
}
