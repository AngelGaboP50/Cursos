package com.example.demo.service;
import com.example.demo.dto.request.*;
import com.example.demo.dto.response.*;
import com.example.demo.exception.*;
import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;
@Service public class AuthService  {
    private final AuthenticationManager am;
    private final UserRepository repo;
    private final PasswordEncoder pe;
    private final JwtTokenService jwt;
    public AuthService(AuthenticationManager am,UserRepository repo,PasswordEncoder pe,JwtTokenService jwt) {
        this.am=am;
        this.repo=repo;
        this.pe=pe;
        this.jwt=jwt;
    }
    @Transactional public UserResponse register(RegisterRequest r) {
        String email=norm(r.email());
        if(repo.existsByEmailIgnoreCase(email))throw new DuplicateEmailException();
        User u=new User(r.name().trim(),email,pe.encode(r.password()),Role.USER);
        return UserResponse.from(repo.saveAndFlush(u));
    }
    @Transactional(readOnly=true) public JwtResponse login(LoginRequest r) {
        String email=norm(r.email());
        try {
            am.authenticate(new UsernamePasswordAuthenticationToken(email,r.password()));
        }
        catch(AuthenticationException e) {
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }
        User u=find(email);
        return new JwtResponse(jwt.generate(u.getId(),u.getName(),u.getEmail(),u.getRole().name()),jwt.expirationMs(),UserResponse.from(u));
    }
    @Transactional(readOnly=true) public UserResponse currentUser(String email) {
        return UserResponse.from(find(email));
    }
    @Transactional public UserResponse updateProfile(String email,ProfileUpdateRequest r) {
        User u=find(email);
        u.setName(r.name().trim());
        return UserResponse.from(u);
    }
    private User find(String e) {
        return repo.findByEmailIgnoreCase(norm(e)).orElseThrow(()->new ResourceNotFoundException("Usuario no encontrado"));
    }
    private String norm(String e) {
        return e.trim().toLowerCase(Locale.ROOT);
    }
}
