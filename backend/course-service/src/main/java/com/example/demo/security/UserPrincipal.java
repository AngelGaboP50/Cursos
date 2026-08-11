package com.example.demo.security;
import java.security.Principal;
public record UserPrincipal(Long userId,String email,String displayName,String role) implements Principal { @Override public String getName(){return email;} }
