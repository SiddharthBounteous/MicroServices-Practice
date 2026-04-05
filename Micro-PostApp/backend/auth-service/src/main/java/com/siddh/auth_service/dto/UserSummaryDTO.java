package com.siddh.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSummaryDTO {
    private Long userId;
    private String username;
}
