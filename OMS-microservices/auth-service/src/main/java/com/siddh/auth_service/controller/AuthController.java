package com.siddh.auth_service.controller;

import com.siddh.auth_service.dto.LoginRequestDTO;
import com.siddh.auth_service.dto.RegisterRequestDTO;
import com.siddh.auth_service.entity.UserEntity;
import com.siddh.auth_service.repository.UserRepository;
import com.siddh.auth_service.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthController(JWTUtil jwtUtil, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.jwtUtil=jwtUtil;
        this.userDetailsService=userDetailsService;
        this.passwordEncoder=passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        if(userRepository.existsByUsername(registerRequestDTO.getUsername())){
            return ResponseEntity.badRequest().body("Error! Username already taken");
        }

        String requestedRole=registerRequestDTO.getRole().toUpperCase();

        if(!requestedRole.startsWith("ROLE_")){
            requestedRole="ROLE_" + requestedRole;
        }

        if(!requestedRole.equals("ROLE_ADMIN") && !requestedRole.equals("ROLE_TRADER") && !requestedRole.equals("ROLE_VIEWER")){
            return ResponseEntity.badRequest().body("Error: Invalid role! Allowed roles are ADMIN, TRADER, VIEWER.");
        }

        UserEntity newUser=new UserEntity();
        newUser.setUsername(registerRequestDTO.getUsername());
        newUser.setRole(requestedRole);
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody LoginRequestDTO requestDTO){
        UserDetails userDetails=userDetailsService.loadUserByUsername(requestDTO.getUsername());
        if(passwordEncoder.matches(requestDTO.getPassword(),userDetails.getPassword())){
            String token=jwtUtil.generateToken(userDetails);
            Map<String,String> response=new HashMap<>();

            response.put("token",token);
            return response;
        }
        else{
            throw new RuntimeException("Invalid Password!");
        }
    }

}
