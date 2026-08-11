package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        @Size(max = 255, message = "El email es demasiado largo")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(max = 72, message = "La contraseña es demasiado larga")
        String password
) {
}
