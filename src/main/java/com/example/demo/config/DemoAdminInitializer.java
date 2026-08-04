package com.example.demo.config;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@ConditionalOnProperty(name = "app.demo-data.enabled", havingValue = "true")
public class DemoAdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public DemoAdminInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.demo-data.admin-email:admin@demo.com}") String adminEmail,
            @Value("${app.demo-data.admin-password:}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        String normalizedEmail = adminEmail == null
                ? ""
                : adminEmail.trim().toLowerCase(Locale.ROOT);

        if (normalizedEmail.isBlank() || adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException(
                    "DEMO_ADMIN_EMAIL y DEMO_ADMIN_PASSWORD son obligatorios cuando DEMO_DATA_ENABLED=true");
        }

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            return;
        }

        User demoAdmin = new User(
                "Administrador Demo",
                normalizedEmail,
                passwordEncoder.encode(adminPassword),
                Role.ADMIN);
        userRepository.saveAndFlush(demoAdmin);
    }
}
