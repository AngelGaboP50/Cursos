package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.JwtResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    /**
     * Autentica al usuario con email y contraseña.
     * Si las credenciales son correctas, genera y retorna un JWT.
     *
     * @throws BadCredentialsException si el email o la contraseña son incorrectos
     */
    public JwtResponse login(LoginRequest request) {
        try {
            // Spring Security verifica el email + contraseña contra la BD
            // Si las credenciales son incorrectas lanza BadCredentialsException automáticamente
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Si llegamos aquí, las credenciales son correctas
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Generar el token JWT
            String token = jwtProvider.generateToken(user.getEmail());

            return new JwtResponse(
                    token,
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole().name()
            );

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }
    }
}
