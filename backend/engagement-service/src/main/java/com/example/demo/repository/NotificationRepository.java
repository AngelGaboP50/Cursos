package com.example.demo.repository;
import com.example.demo.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long u);
    Optional<Notification> findByIdAndUserId(Long id,Long u);
    long countByUserIdAndReadFalse(Long u);
}
