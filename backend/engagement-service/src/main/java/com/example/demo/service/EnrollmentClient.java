package com.example.demo.service;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import com.example.demo.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import java.util.Map;
@Service public class EnrollmentClient {
    private final RestClient c;
    private final String secret;
    public EnrollmentClient(@LoadBalanced RestClient.Builder b,@Value("${app.internal.secret}")String s) {
        c=b.build();
        secret=s;
    }
    public boolean hasAccess(Long uid,Long cid) {
        try {
            Map x=c.get().uri("http://enrollment-service/internal/enrollments/access?userId={u}&courseId={c}",uid,cid).header("X-Internal-Secret",secret).retrieve().body(Map.class);
            return x!=null&&Boolean.TRUE.equals(x.get("access"));
        }
        catch(RuntimeException e) {
            throw new ServiceUnavailableException("El servicio de inscripciones no está disponible");
        }
    }
}
