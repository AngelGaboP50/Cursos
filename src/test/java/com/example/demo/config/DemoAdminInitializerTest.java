package com.example.demo.config;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DemoAdminInitializerTest {

    @Mock
    UserRepository userRepository;

    @Test
    void createsEnabledAdminWithBcryptPassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String runtimePassword = testPassword();
        DemoAdminInitializer initializer = new DemoAdminInitializer(
                userRepository, encoder, " ADMIN@DEMO.COM ", runtimePassword);
        when(userRepository.existsByEmailIgnoreCase("admin@demo.com")).thenReturn(false);

        initializer.run(null);

        ArgumentCaptor<User> savedUser = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveAndFlush(savedUser.capture());
        User admin = savedUser.getValue();
        assertThat(admin.getEmail()).isEqualTo("admin@demo.com");
        assertThat(admin.getRole()).isEqualTo(Role.ADMIN);
        assertThat(admin.isEnabled()).isTrue();
        assertThat(admin.getPassword()).startsWith("$2");
        assertThat(encoder.matches(runtimePassword, admin.getPassword())).isTrue();
    }

    @Test
    void doesNotDuplicateAnExistingAdmin() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        DemoAdminInitializer initializer = new DemoAdminInitializer(
                userRepository, encoder, "admin@demo.com", testPassword());
        when(userRepository.existsByEmailIgnoreCase("admin@demo.com")).thenReturn(true);

        initializer.run(null);

        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void repeatedRunsCreateTheAdminOnlyOnce() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        DemoAdminInitializer initializer = new DemoAdminInitializer(
                userRepository, encoder, "admin@demo.com", testPassword());
        when(userRepository.existsByEmailIgnoreCase("admin@demo.com"))
                .thenReturn(false, true);

        initializer.run(null);
        initializer.run(null);

        verify(userRepository, times(1)).saveAndFlush(any(User.class));
    }

    private String testPassword() {
        return "T3st-" + UUID.randomUUID() + "-Aa!";
    }
}
