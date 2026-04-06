package com.siddh.interaction_service.client;

import com.siddh.interaction_service.dto.UserSummaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="auth-service")
public interface AuthClient {
    @GetMapping("/api/v1/users/{id}/summary")
    UserSummaryDTO getUserSummary(@PathVariable("id") Long id);
}
