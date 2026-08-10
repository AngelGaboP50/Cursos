package com.example.demo.service;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import com.example.demo.dto.response.ReportSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import java.util.*;
@Service public class ReportService {
    private final RestClient c;
    private final String secret;
    public ReportService(@LoadBalanced RestClient.Builder b,@Value("${app.internal.secret}")String s) {
        c=b.build();
        secret=s;
    }
    public ReportSummary summary() {
        Map<String,Long>u=get("http://auth-user-service/internal/users/stats");
        Map<String,Long>co=get("http://course-service/internal/courses/stats");
        Map<String,Long>en=get("http://enrollment-service/internal/enrollments/stats");
        Map<String,Long>eg=get("http://engagement-service/internal/engagement/stats");
        return new ReportSummary(v(u,"users"),v(co,"courses"),v(co,"publishedCourses"),v(en,"enrollments"),v(en,"activeEnrollments"),v(en,"completedEnrollments"),v(eg,"notifications"),v(eg,"reviews"));
    }
    public byte[] coursesCsv() {
        return bytes("http://course-service/internal/courses/export");
    }
    public byte[] enrollmentsCsv() {
        return bytes("http://enrollment-service/internal/enrollments/export");
    }
    private Map<String,Long>get(String uri) {
        try {
            Map raw=c.get().uri(uri).header("X-Internal-Secret",secret).retrieve().body(Map.class);
            Map<String,Long>x=new HashMap<>();
            if(raw!=null)raw.forEach((k,v)-> {
                if(v instanceof Number n)x.put(String.valueOf(k),n.longValue());
            }
            );
            return x;
        }
        catch(RuntimeException e) {
            return Map.of();
        }
    }
    private long v(Map<String,Long>x,String k) {
        return x.getOrDefault(k,0L);
    }
    private byte[] bytes(String uri) {
        try {
            return c.get().uri(uri).header("X-Internal-Secret",secret).retrieve().body(byte[].class);
        }
        catch(RuntimeException e) {
            return ("Servicio dependiente no disponible\n").getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}
