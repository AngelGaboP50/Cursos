package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Archivo de respuesta JWT
//Sirve para responder al login y registro de usuarios
//Tiene validaciones para el correo electrónico y la contraseña
//Tiene validaciones para el nombre y la contraseña

@Data
@Builder
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private String email;
    private String role;

    public JwtResponse(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}