package com.example.demo.security;
import com.example.demo.exception.ApiError;
import jakarta.servlet.http.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
@Component public class RestAccessDeniedHandler implements AccessDeniedHandler  {
    private final ObjectMapper m;
    public RestAccessDeniedHandler(ObjectMapper m) {
        this.m=m;
    }
    public void handle(HttpServletRequest r,HttpServletResponse s,AccessDeniedException e)throws IOException {
        s.setStatus(403);
        s.setContentType(MediaType.APPLICATION_JSON_VALUE);
        m.writeValue(s.getWriter(),ApiError.of(403,"Forbidden","No tienes permisos para este recurso",r.getRequestURI()));
    }
}
