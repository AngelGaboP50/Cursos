package com.example.demo.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="notifications") public class Notification {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)private Long id;
    @Column(name="user_id",nullable=false)private Long userId;
    @Column(nullable=false,length=160)private String title;
    @Column(nullable=false,length=1000)private String message;
    @Column(name="is_read",nullable=false)private boolean read;
    @Column(name="created_at",nullable=false,updatable=false)private Instant createdAt;
    protected Notification() {
    }
    public Notification(Long uid,String t,String m) {
        userId=uid;
        title=t;
        message=m;
    }
    @PrePersist void p() {
        if(createdAt==null)createdAt=Instant.now();
    }
    public Long getId() {
        return id;
    }
    public Long getUserId() {
        return userId;
    }
    public String getTitle() {
        return title;
    }
    public String getMessage() {
        return message;
    }
    public boolean isRead() {
        return read;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void markRead() {
        read=true;
    }
}
