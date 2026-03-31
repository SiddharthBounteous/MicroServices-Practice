package com.siddh.interaction_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 140, message = "Comment strictly cannot exceed 140 characters")
    private String content;
}
