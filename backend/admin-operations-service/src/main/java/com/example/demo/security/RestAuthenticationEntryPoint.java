package com.example.demo.security;
import com.example.demo.exception.ApiError;
import jakarta.servlet.http.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
@Component public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint  {
    private final ObjectMapper m;
    public RestAuthenticationEntryPoint(ObjectMapper m) {
        this.m=m;
    }
    public void commence(HttpServletRequest r,HttpServletResponse s,AuthenticationException e)throws IOException {
        s.setStatus(401);
        s.setContentType(MediaType.APPLICATION_JSON_VALUE);
        m.writeValue(s.getWriter(),ApiError.of(401,"Unauthorized","Se requiere autenticación",r.getRequestURI()));
    }
}
