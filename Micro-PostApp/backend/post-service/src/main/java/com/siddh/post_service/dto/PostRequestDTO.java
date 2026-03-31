package com.siddh.post_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostRequestDTO{
    @NotBlank(message = "Post cannot be empty")
    @Size(max = 140, message = "Post strictly cannot exceed 140 characters")
    private String content;

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content=content;
    }
}
