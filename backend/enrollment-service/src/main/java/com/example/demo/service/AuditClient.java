package com.example.demo.service;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import java.util.Map;
@Service public class AuditClient  {
    private final RestClient client;
    private final String secret;
    public AuditClient(@LoadBalanced RestClient.Builder b,@Value("${app.internal.secret}")String secret) {
        this.client=b.build();
        this.secret=secret;
    }
    public void record(Long uid,String email,String action,String type,Object entityId,String details) {
        try {
            client.post().uri("http://admin-operations-service/internal/audit").header("X-Internal-Secret",secret).body(Map.of("actorUserId",uid==null?0L:uid,"actorEmail",email==null?"system":email,"action",action,"entityType",type,"entityId",entityId==null?"":entityId.toString(),"details",details==null?"":details)).retrieve().toBodilessEntity();
        }
        catch(RuntimeException ignored) {
        }
    }
}
