package com.siddh.api_gateway.filter;

import com.siddh.api_gateway.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.http.HttpHeaders;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public AuthenticationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path=request.getRequestURI();

        if(path.contains("/api/auth/login")){
            filterChain.doFilter(request,response);
            return;
        }

        String authHeader=request.getHeader("Authorization");

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Missing or Invalid Authorization Header"); // Give Postman a nice message!
            return;
        }

        String token=authHeader.substring(7);

        try{
            jwtUtil.validateToken(token);
        }
        catch(Exception e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token is Fake or Expired!"); // Give Postman a nice message!
            return;
        }

        filterChain.doFilter(request,response);
    }
}
