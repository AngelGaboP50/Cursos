package com.example.demo.service;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import java.util.Map;
@Service public class EngagementClient  {
    private final RestClient c;
    private final String secret;
    public EngagementClient(@LoadBalanced RestClient.Builder b,@Value("${app.internal.secret}")String s) {
        c=b.build();
        secret=s;
    }
    public void notify(Long uid,String title,String msg) {
        try {
            c.post().uri("http://engagement-service/internal/notifications").header("X-Internal-Secret",secret).body(Map.of("userId",uid,"title",title,"message",msg)).retrieve().toBodilessEntity();
        }
        catch(RuntimeException ignored) {
        }
    }
}
