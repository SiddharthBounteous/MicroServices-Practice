package com.siddh.auth_service.service;

import com.siddh.auth_service.dto.LoginRequestDTO;
import com.siddh.auth_service.dto.RegisterRequestDTO;
import com.siddh.auth_service.entity.UserEntity;
import com.siddh.auth_service.entity.VerificationToken;
import com.siddh.auth_service.repository.TokenRepository;
import com.siddh.auth_service.repository.UserRepository;
import com.siddh.auth_service.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final MailService mailService;

    public ResponseEntity<String> register(RegisterRequestDTO registerRequestDTO){
        if(userRepository.existsByUsername(registerRequestDTO.getUsername())){
            return ResponseEntity.badRequest().body("Error! Username already taken");
        }

        if(userRepository.existsByEmail(registerRequestDTO.getEmail())){
            return ResponseEntity.badRequest().body("Error! Email already registered");
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
        newUser.setEmail(registerRequestDTO.getEmail());
        newUser.setRole(requestedRole);
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        newUser.setEnabled(false);
        newUser.setEmailVerified(false);

        userRepository.save(newUser);

        String tokenValue=UUID.randomUUID().toString();

        VerificationToken verificationToken=VerificationToken.builder()
                .token(tokenValue)
                .user(newUser)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .used(false)
                .build();

        tokenRepository.save(verificationToken);
        mailService.sendVerificationMail(newUser.getEmail(),tokenValue);
        return ResponseEntity.ok("User registered successfully! Please verify your email");
    }

    public ResponseEntity<?> login(LoginRequestDTO loginRequestDTO){

        UserEntity userEntity=userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(()->new RuntimeException("User not found"));

        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), userEntity.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password!");
        }

        if(!userEntity.isEnabled() || !userEntity.isEmailVerified()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Please verify your email before logging in");
        }

        UserDetails userDetails=userDetailsService.loadUserByUsername(loginRequestDTO.getUsername());
        String token=jwtUtil.generateToken(userDetails, userEntity.getId());

        Map<String, String> response=new HashMap<>();
        response.put("userId", String.valueOf(userEntity.getId()));
        response.put("username", userEntity.getUsername());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> verifyEmail(String tokenValue){
        VerificationToken verificationToken=tokenRepository.findByToken(tokenValue)
                .orElseThrow(()->new RuntimeException("Invalid Token"));

        if(verificationToken.isUsed()){
            return ResponseEntity.badRequest().body("Verification Token already used");
        }

        if(verificationToken.getExpiryDate().isBefore(LocalDateTime.now())){
            return ResponseEntity.badRequest().body("Verification token expired");
        }

        UserEntity user=verificationToken.getUser();
        user.setEmailVerified(true);
        user.setEnabled(true);
        userRepository.save(user);
        verificationToken.setUsed(true);
        tokenRepository.save(verificationToken);

        return ResponseEntity.ok("Email verified successfully");
    }
}
