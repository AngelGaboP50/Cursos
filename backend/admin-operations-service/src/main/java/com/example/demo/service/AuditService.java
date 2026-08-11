package com.example.demo.service;
import com.example.demo.dto.response.AuditEventResponse;
import com.example.demo.model.AuditEvent;
import com.example.demo.repository.AuditEventRepository;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class AuditService {
    private final AuditEventRepository r;
    public AuditService(AuditEventRepository r) {
        this.r=r;
    }
    public void record(Long uid,String email,String action,String type,String eid,String details) {
        r.save(new AuditEvent(uid,email,action,type,eid,details));
    }
    public List<AuditEventResponse> recent() {
        return r.findTop100ByOrderByCreatedAtDesc().stream().map(AuditEventResponse::from).toList();
    }
}
