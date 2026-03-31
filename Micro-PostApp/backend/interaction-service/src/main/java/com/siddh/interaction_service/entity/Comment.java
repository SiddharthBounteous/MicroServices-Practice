package com.siddh.interaction_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //ID of the post from the post-service
    @Column(name = "post_id", nullable = false)
    private Long postId;

    //ID of the user from the auth-service
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 140)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
