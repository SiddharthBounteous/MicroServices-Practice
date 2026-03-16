package com.siddh.auth_service.service;

import com.siddh.auth_service.entity.UserEntity;
import com.siddh.auth_service.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
    }

//    private final PasswordEncoder passwordEncoder;
//
//    public CustomUserDetailService(PasswordEncoder passwordEncoder){
//        this.passwordEncoder=passwordEncoder;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        if("admin".equals(username)){
//            return User.builder()
//                    .username("admin")
//                    .password(passwordEncoder.encode("pass123"))
//                    .roles("ADMIN")
//                    .build();
//        }
//        else if("trader".equals(username)){
//            return User.builder()
//                    .username("trader")
//                    .password(passwordEncoder.encode("trader123"))
//                    .roles("TRADER")
//                    .build();
//        }
//        else if("viewer".equals(username)){
//            return User.builder()
//                    .username("viewer")
//                    .password(passwordEncoder.encode("viewer123"))
//                    .roles("VIEWER")
//                    .build();
//        }
//        else{
//            throw new UsernameNotFoundException("User not found in the system: " + username);
//        }
//    }


}
