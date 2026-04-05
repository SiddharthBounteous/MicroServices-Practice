package com.siddh.auth_service.controller;

import com.siddh.auth_service.dto.UserSummaryDTO;
import com.siddh.auth_service.entity.UserEntity;
import com.siddh.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}/summary")
    public ResponseEntity<UserSummaryDTO> getUserSummary(@PathVariable Long id){
        System.out.println("HIT getUserSummary controller, id = " + id);
        UserEntity user=userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(new UserSummaryDTO(user.getId(), user.getUsername()));
    }
}