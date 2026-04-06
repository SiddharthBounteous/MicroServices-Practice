package com.siddh.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
}
