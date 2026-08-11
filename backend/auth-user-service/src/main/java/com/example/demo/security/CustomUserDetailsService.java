package com.example.demo.security;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service public class CustomUserDetailsService implements UserDetailsService  {
    private final UserRepository repo;
    public CustomUserDetailsService(UserRepository repo) {
        this.repo=repo;
    }
    public UserDetails loadUserByUsername(String email) {
        var u=repo.findByEmailIgnoreCase(email).orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));
        return org.springframework.security.core.userdetails.User.withUsername(u.getEmail()).password(u.getPassword()).roles(u.getRole().name()).disabled(!u.isEnabled()).build();
    }
}
