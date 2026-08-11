package com.example.demo.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
@Component public class JwtTokenService  {
    private final SecretKey key;
    private final long expirationMs;
    public JwtTokenService(@Value("${jwt.secret}") String secret,@Value("${jwt.expiration-ms:3600000}") long expirationMs) {
        if(secret==null||secret.getBytes(StandardCharsets.UTF_8).length<32)throw new IllegalStateException("JWT_SECRET debe tener al menos 32 bytes");
        this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs=expirationMs;
    }
    public String generate(Long id,String name,String email,String role) {
        Instant now=Instant.now();
        return Jwts.builder().subject(email).claim("userId",id).claim("name",name).claim("role",role).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(expirationMs))).signWith(key).compact();
    }
    public UserPrincipal parse(String token) {
        var c=Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        if(c.getExpiration()==null||!c.getExpiration().after(new Date())) throw new JwtException("Token expirado");
        Object rawId=c.get("userId");
        Long id=rawId instanceof Number n?n.longValue():(rawId==null?null:Long.valueOf(rawId.toString()));
        String role=c.get("role",String.class);
        String name=c.get("name",String.class);
        return new UserPrincipal(id,c.getSubject(),name==null?c.getSubject():name,role==null?"USER":role);
    }
    public long expirationMs() {
        return expirationMs;
    }
}
