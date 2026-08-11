package com.example.demo.service;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import com.example.demo.dto.response.CourseDTO;
import com.example.demo.exception.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
@Service public class CourseClient  {
    private final RestClient c;
    private final String secret;
    public CourseClient(@LoadBalanced RestClient.Builder b,@Value("${app.internal.secret}")String s) {
        c=b.build();
        secret=s;
    }
    public CourseDTO require(Long id) {
        try {
            CourseDTO x=c.get().uri("http://course-service/internal/courses/{id}",id).header("X-Internal-Secret",secret).retrieve().body(CourseDTO.class);
            if(x==null)throw new ResourceNotFoundException("Curso no encontrado");
            return x;
        }
        catch(HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Curso no encontrado");
        }
        catch(RuntimeException ex) {
            throw new ServiceUnavailableException("El servicio de cursos no está disponible");
        }
    }
}
