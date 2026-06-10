package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.JwtResponse;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     *
     * Body (JSON):
     * {
     *   "email": "usuario@ejemplo.com",
     *   "password": "miContraseña123"
     * }
     *
     * Response 200:
     * {
     *   "token": "eyJhbGc...",
     *   "type": "Bearer",
     *   "userId": 1,
     *   "email": "usuario@ejemplo.com",
     *   "name": "Juan Pérez",
     *   "role": "STUDENT"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.login(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "error", "Credenciales inválidas",
                            "message", "El email o la contraseña son incorrectos"
                    ));
        }
    }
}
