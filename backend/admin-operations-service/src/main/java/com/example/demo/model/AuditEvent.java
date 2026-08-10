package com.example.demo.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="audit_events") public class AuditEvent {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)private Long id;
    @Column(name="actor_user_id")private Long actorUserId;
    @Column(name="actor_email",nullable=false,length=255)private String actorEmail;
    @Column(nullable=false,length=80)private String action;
    @Column(name="entity_type",nullable=false,length=80)private String entityType;
    @Column(name="entity_id",length=100)private String entityId;
    @Column(nullable=false,length=1000)private String details;
    @Column(name="created_at",nullable=false,updatable=false)private Instant createdAt;
    protected AuditEvent() {
    }
    public AuditEvent(Long uid,String email,String action,String type,String eid,String details) {
        actorUserId=uid;
        actorEmail=email;
        this.action=action;
        entityType=type;
        entityId=eid;
        this.details=details;
    }
    @PrePersist void p() {
        if(createdAt==null)createdAt=Instant.now();
    }
    public Long getId() {
        return id;
    }
    public Long getActorUserId() {
        return actorUserId;
    }
    public String getActorEmail() {
        return actorEmail;
    }
    public String getAction() {
        return action;
    }
    public String getEntityType() {
        return entityType;
    }
    public String getEntityId() {
        return entityId;
    }
    public String getDetails() {
        return details;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
}
