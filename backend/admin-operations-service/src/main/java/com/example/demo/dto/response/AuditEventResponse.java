package com.example.demo.dto.response;
import com.example.demo.model.AuditEvent;
import java.time.Instant;
public record AuditEventResponse(Long id,String actorEmail,String action,String entityType,String entityId,String details,Instant createdAt) {
    public static AuditEventResponse from(AuditEvent e) {
        return new AuditEventResponse(e.getId(),e.getActorEmail(),e.getAction(),e.getEntityType(),e.getEntityId(),e.getDetails(),e.getCreatedAt());
    }
}
