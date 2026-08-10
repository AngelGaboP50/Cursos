package com.example.demo.controller;
import com.example.demo.dto.response.AuditEventResponse;
import com.example.demo.service.AuditService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;
@RestController public class OperationsController {
    private final AuditService a;
    private final JdbcTemplate jdbc;
    public OperationsController(AuditService a,JdbcTemplate jdbc) {
        this.a=a;
        this.jdbc=jdbc;
    }
    @GetMapping("/api/admin/audit")List<AuditEventResponse> audit() {
        return a.recent();
    }
    @GetMapping("/api/public/health")Map<String,Object> health() {
        Integer x=jdbc.queryForObject("select 1",Integer.class);
        return Map.of("status","UP","database",x!=null&&x==1?"UP":"DOWN","timestamp",Instant.now().toString());
    }
}
