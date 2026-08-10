package com.example.demo.controller;

import com.example.demo.security.InternalAuth;
import com.example.demo.service.AuditService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/audit")
public class InternalAuditController {
    private final AuditService auditService;
    private final InternalAuth internalAuth;

    public InternalAuditController(AuditService auditService, InternalAuth internalAuth) {
        this.auditService = auditService;
        this.internalAuth = internalAuth;
    }

    @PostMapping
    void record(@RequestBody AuditCreateRequest request,
                @RequestHeader("X-Internal-Secret") String secret) {
        internalAuth.check(secret);
        auditService.record(
                request.actorUserId(), request.actorEmail(), request.action(),
                request.entityType(), request.entityId(), request.details());
    }

    public record AuditCreateRequest(
            Long actorUserId,
            String actorEmail,
            String action,
            String entityType,
            String entityId,
            String details) {
    }
}
