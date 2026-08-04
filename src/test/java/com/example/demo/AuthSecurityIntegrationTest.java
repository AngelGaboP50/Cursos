package com.example.demo;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthSecurityIntegrationTest {

    private static final String JWT_SECRET =
            "test-secret-with-more-than-thirty-two-characters-123456";
    private static final String TEST_PASSWORD =
            "T3st-" + UUID.randomUUID() + "-Aa!";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanUsers() {
        userRepository.deleteAll();
    }

    @Test
    void registersValidUserWithBcryptAndSafeResponse() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson("Persona Demo", "persona@demo.com", TEST_PASSWORD)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("persona@demo.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.password").doesNotExist());

        User persisted = userRepository.findByEmailIgnoreCase("persona@demo.com").orElseThrow();
        assertThat(persisted.getPassword()).startsWith("$2");
        assertThat(passwordEncoder.matches(TEST_PASSWORD, persisted.getPassword())).isTrue();
    }

    @Test
    void rejectsDuplicateEmailWith409() throws Exception {
        saveUser("Existente", "duplicado@demo.com", Role.USER);
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson("Otra persona", "DUPLICADO@demo.com", TEST_PASSWORD)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void rejectsInvalidEmailWith400() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson("Persona Demo", "correo-invalido", TEST_PASSWORD)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void rejectsWeakPasswordWith400() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson("Persona Demo", "persona@demo.com", "solominusculas")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.password").exists());
    }

    @Test
    void rejectsMalformedJsonWith400AndSafeBody() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not-json}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("El cuerpo JSON no es válido"))
                .andExpect(jsonPath("$.trace").doesNotExist());
    }

    @Test
    void logsInWithValidCredentialsAndReturnsJwt() throws Exception {
        saveUser("Persona Demo", "persona@demo.com", Role.USER);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson("persona@demo.com", TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.expiresInMs").value(3600000))
                .andExpect(jsonPath("$.user.role").value("USER"))
                .andExpect(jsonPath("$.user.password").doesNotExist());
    }

    @Test
    void rejectsInvalidCredentialsWith401() throws Exception {
        saveUser("Persona Demo", "persona@demo.com", Role.USER);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson("persona@demo.com", TEST_PASSWORD + "-incorrect")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void rejectsProtectedEndpointWithoutTokenWith401() throws Exception {
        mockMvc.perform(get("/api/account/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void rejectsInvalidTokenWith401() throws Exception {
        mockMvc.perform(get("/api/account/me")
                        .header("Authorization", "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsExpiredTokenWith401() throws Exception {
        saveUser("Persona Demo", "persona@demo.com", Role.USER);
        String token = Jwts.builder()
                .subject("persona@demo.com")
                .issuedAt(Date.from(Instant.now().minusSeconds(120)))
                .expiration(Date.from(Instant.now().minusSeconds(60)))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
        mockMvc.perform(get("/api/account/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deniesAdminEndpointToUserWith403() throws Exception {
        saveUser("Persona Demo", "persona@demo.com", Role.USER);
        mockMvc.perform(get("/api/admin/security-check")
                        .header("Authorization", "Bearer " + loginToken("persona@demo.com")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void permitsAdminEndpointToAdmin() throws Exception {
        saveUser("Administración", "admin@demo.com", Role.ADMIN);
        mockMvc.perform(get("/api/admin/security-check")
                        .header("Authorization", "Bearer " + loginToken("admin@demo.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Acceso administrativo autorizado"));
    }

    @Test
    void logoutRequiresTokenAndReturns204() throws Exception {
        saveUser("Persona Demo", "persona@demo.com", Role.USER);
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + loginToken("persona@demo.com")))
                .andExpect(status().isNoContent());
    }

    private void saveUser(String name, String email, Role role) {
        userRepository.save(new User(name, email,
                passwordEncoder.encode(TEST_PASSWORD), role));
    }

    private String loginToken(String email) throws Exception {
        String json = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson(email, TEST_PASSWORD)))
                .andReturn().getResponse().getContentAsString();
        int start = json.indexOf("\"token\":\"") + 9;
        return json.substring(start, json.indexOf('"', start));
    }

    private String registerJson(String name, String email, String password) {
        return """
                {"name":"%s","email":"%s","password":"%s"}
                """.formatted(name, email, password);
    }

    private String loginJson(String email, String password) {
        return """
                {"email":"%s","password":"%s"}
                """.formatted(email, password);
    }
}
