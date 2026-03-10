package com.siddh.auth_service.controller;

import com.siddh.auth_service.dto.LoginRequestDTO;
import com.siddh.auth_service.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    public AuthController(JWTUtil jwtUtil, UserDetailsService userDetailsService,PasswordEncoder passwordEncoder){
        this.jwtUtil=jwtUtil;
        this.userDetailsService=userDetailsService;
        this.passwordEncoder=passwordEncoder;
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
