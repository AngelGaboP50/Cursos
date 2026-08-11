package com.example.demo.controller;
import com.example.demo.dto.request.ProfileUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/account") public class AccountController  {
    private final AuthService s;
    public AccountController(AuthService s) {
        this.s=s;
    }
    @GetMapping("/me") UserResponse me(Authentication a) {
        return s.currentUser(a.getName());
    }
    @PatchMapping("/me") UserResponse update(@Valid @RequestBody ProfileUpdateRequest r,Authentication a) {
        return s.updateProfile(a.getName(),r);
    }
}
