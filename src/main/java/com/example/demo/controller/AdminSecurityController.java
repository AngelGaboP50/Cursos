package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminSecurityController {

    @GetMapping("/security-check")
    public Map<String, String> securityCheck(Authentication authentication) {
        return Map.of(
                "message", "Acceso administrativo autorizado",
                "user", authentication.getName());
    }
}
