package com.example.demo.service;

import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String TEST_PASSWORD =
            "T3st-" + UUID.randomUUID() + "-Aa!";

    @Mock AuthenticationManager authenticationManager;
    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtProvider jwtProvider;

    @Test
    void registrationNormalizesEmailAndEncodesPassword() {
        AuthService service = service();
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn("bcrypt-hash");
        when(userRepository.saveAndFlush(org.mockito.ArgumentMatchers.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.register(new RegisterRequest(
                " Persona Demo ", " PERSONA@DEMO.COM ", TEST_PASSWORD));

        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveAndFlush(user.capture());
        assertThat(user.getValue().getEmail()).isEqualTo("persona@demo.com");
        assertThat(user.getValue().getName()).isEqualTo("Persona Demo");
        assertThat(user.getValue().getPassword()).isEqualTo("bcrypt-hash");
        assertThat(user.getValue().getRole().name()).isEqualTo("USER");
    }

    @Test
    void duplicateRegistrationStopsBeforeEncoding() {
        AuthService service = service();
        when(userRepository.existsByEmailIgnoreCase("persona@demo.com")).thenReturn(true);

        assertThatThrownBy(() -> service.register(new RegisterRequest(
                "Persona Demo", "persona@demo.com", TEST_PASSWORD)))
                .isInstanceOf(DuplicateEmailException.class);

        verifyNoInteractions(passwordEncoder);
    }

    private AuthService service() {
        return new AuthService(authenticationManager, userRepository, passwordEncoder, jwtProvider);
    }
}
