package com.siddh.auth_service.controller;

import com.siddh.auth_service.dto.LoginRequestDTO;
import com.siddh.auth_service.dto.RegisterRequestDTO;
import com.siddh.auth_service.entity.UserEntity;
import com.siddh.auth_service.repository.UserRepository;
import com.siddh.auth_service.service.AuthService;
import com.siddh.auth_service.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        return authService.register(registerRequestDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO requestDTO){
        return authService.login(requestDTO);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token){
        return authService.verifyEmail(token);
    }

}
