package com.siddh.api_gateway.filter;

import com.siddh.api_gateway.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.*;

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

            Long userId=jwtUtil.extractUserId(token);

            if (roles == null || roles.isEmpty()){
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Access Denied: Your token does not contain any roles. Please log in again!");
                return;
            }
            if(path.contains("/api/v1/admin") && !roles.contains("ROLE_ADMIN")){
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Access Denied: You must be admin to perform the operation");
                return;
            }

            HeaderMapRequestWrapper wrappedRequest=new HeaderMapRequestWrapper(request);
            wrappedRequest.addHeader("X-User-Id", String.valueOf(userId));
            filterChain.doFilter(wrappedRequest, response);
        }
        catch(Exception e){
            System.out.println("JWT SECURITY ERROR: " + e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token is Fake or Expired!");
        }

        filterChain.doFilter(request,response);
    }

    public static class HeaderMapRequestWrapper extends HttpServletRequestWrapper{

        private Map<String,String> headerMap=new HashMap<>();
        public HeaderMapRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        public void addHeader(String name, String value){
            headerMap.put(name,value);
        }

        @Override
        public String getHeader(String name){
            String headerValue=headerMap.get(name);
            if (headerValue!=null){
                return headerValue;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String>getHeaderNames(){
            List<String> names= Collections.list(super.getHeaderNames());
            names.addAll(headerMap.keySet());
            return Collections.enumeration(names);
        }

        @Override
        public Enumeration<String> getHeaders(String name){
            List<String>values=Collections.list(super.getHeaders(name));
            if(headerMap.containsKey(name)){
                values.add(headerMap.get(name));
            }
            return Collections.enumeration(values);
        }
    }
}
