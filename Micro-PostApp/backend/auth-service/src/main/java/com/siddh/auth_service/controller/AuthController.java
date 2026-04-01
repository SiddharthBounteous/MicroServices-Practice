package com.siddh.auth_service.controller;

import com.siddh.auth_service.dto.LoginRequestDTO;
import com.siddh.auth_service.dto.RegisterRequestDTO;
import com.siddh.auth_service.entity.UserEntity;
import com.siddh.auth_service.repository.UserRepository;
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

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        if(userRepository.existsByUsername(registerRequestDTO.getUsername())){
            return ResponseEntity.badRequest().body("Error! Username already taken");
        }

        String requestedRole=registerRequestDTO.getRole().toUpperCase();

        if(requestedRole==null || requestedRole.trim().isEmpty()){
            requestedRole="ROLE_USER";
        }
        else{
            requestedRole=requestedRole.toUpperCase();
            if(!requestedRole.startsWith("ROLE_")){
                requestedRole="ROLE_" + requestedRole;
            }
        }

        if(!requestedRole.equals("ROLE_ADMIN") && !requestedRole.equals("ROLE_USER")){
            return ResponseEntity.badRequest().body("Error: Invalid role! Allowed roles are ADMIN and USER");
        }

        UserEntity newUser=new UserEntity();
        newUser.setUsername(registerRequestDTO.getUsername());
        newUser.setRole(requestedRole);
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO requestDTO){
        UserDetails userDetails=userDetailsService.loadUserByUsername(requestDTO.getUsername());
        if(passwordEncoder.matches(requestDTO.getPassword(),userDetails.getPassword())){

            UserEntity userEntity=userRepository.findByUsername(requestDTO.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String token=jwtUtil.generateToken(userDetails,userEntity.getId());
            Map<String,String> response=new HashMap<>();

            response.put("userId", String.valueOf(userEntity.getId()));
            response.put("username", userEntity.getUsername());
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password!");
        }
    }

}
