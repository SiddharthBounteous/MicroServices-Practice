package com.siddh.post_service.client;

import com.siddh.post_service.dto.UserSummaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/api/v1/users/{id}/summary")
    UserSummaryDTO getUserSummary(@PathVariable("id") Long id, @RequestHeader("Authorization") String authorizationHeader);
}