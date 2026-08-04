package com.example.demo.dto.response;

public record JwtResponse(
        String token,
        String type,
        long expiresInMs,
        UserResponse user
) {
    public JwtResponse(String token, long expiresInMs, UserResponse user) {
        this(token, "Bearer", expiresInMs, user);
    }
}
