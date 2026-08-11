package com.example.demo.controller;
import com.example.demo.dto.request.*;
import com.example.demo.dto.response.*;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") public class AuthController  {
    private final AuthService s;
    public AuthController(AuthService s) {
        this.s=s;
    }
    @PostMapping("/register") ResponseEntity<UserResponse> reg(@Valid @RequestBody RegisterRequest r) {
        return ResponseEntity.status(201).body(s.register(r));
    }
    @PostMapping("/login") JwtResponse login(@Valid @RequestBody LoginRequest r) {
        return s.login(r);
    }
    @PostMapping("/logout") ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }
}
