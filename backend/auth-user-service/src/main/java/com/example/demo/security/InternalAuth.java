package com.example.demo.security;
import com.example.demo.exception.InternalAccessDeniedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
@Component public class InternalAuth  {
    private final String expected;
    public InternalAuth(@Value("${app.internal.secret}") String expected) {
        this.expected=expected;
    }
    public void check(String value) {
        if(value==null||!MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8),value.getBytes(StandardCharsets.UTF_8)))throw new InternalAccessDeniedException();
    }
}
