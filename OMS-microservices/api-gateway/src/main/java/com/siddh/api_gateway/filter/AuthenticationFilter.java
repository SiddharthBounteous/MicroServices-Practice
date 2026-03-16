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
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public AuthenticationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path=request.getRequestURI();

        if (path.contains("/api/auth/login") || path.contains("/api/auth/register")){
            filterChain.doFilter(request, response);
            return;
        }


        String authHeader=request.getHeader("Authorization");

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Missing or Invalid Authorization Header");
            return;
        }

        String token=authHeader.substring(7);

        try{
            jwtUtil.validateToken(token);

            List<String> roles = jwtUtil.extractRoles(token);

            if (roles == null || roles.isEmpty()){
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Access Denied: Your token does not contain any roles. Please log in again!");
                return;
            }
            if(path.contains("/api/v1/orders") && roles.contains("ROLE_VIEWER")){
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Access Denied: Viewers are not allowed to place or view orders!");
                return;
            }

            if(path.contains("/api/v1/analytics") && roles.contains("ROLE_TRADER")){
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Access Denied: Traders cannot view system analytics!");
                return;
            }
        }
        catch(Exception e){
            System.out.println("JWT SECURITY ERROR: " + e.getMessage());
            e.printStackTrace();

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token is Fake or Expired!");
            return;
        }

        filterChain.doFilter(request,response);
    }
}
