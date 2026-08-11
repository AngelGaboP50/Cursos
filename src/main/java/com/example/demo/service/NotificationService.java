package com.example.demo.service;
import com.example.demo.dto.response.NotificationResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service public class NotificationService {
    private final NotificationRepository r;
    public NotificationService(NotificationRepository r) {
        this.r=r;
    }
    public void create(Long uid,String t,String m) {
        r.save(new Notification(uid,t,m));
    }
    @Transactional(readOnly=true)public List<NotificationResponse> list(Long uid) {
        return r.findByUserIdOrderByCreatedAtDesc(uid).stream().map(NotificationResponse::from).toList();
    }
    @Transactional(readOnly=true)public long unread(Long uid) {
        return r.countByUserIdAndReadFalse(uid);
    }
    @Transactional public NotificationResponse read(Long id,Long uid) {
        Notification n=r.findByIdAndUserId(id,uid).orElseThrow(()->new ResourceNotFoundException("Notificación no encontrada"));
        n.markRead();
        return NotificationResponse.from(n);
    }
    @Transactional public void readAll(Long uid) {
        r.findByUserIdOrderByCreatedAtDesc(uid).forEach(Notification::markRead);
    }
}
