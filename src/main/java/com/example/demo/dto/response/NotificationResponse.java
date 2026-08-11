package com.example.demo.dto.response;
import com.example.demo.model.Notification;
import java.time.Instant;
public record NotificationResponse(Long id,String title,String message,boolean read,Instant createdAt) {
    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(n.getId(),n.getTitle(),n.getMessage(),n.isRead(),n.getCreatedAt());
    }
}
