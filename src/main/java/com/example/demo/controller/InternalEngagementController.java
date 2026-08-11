package com.example.demo.controller;

import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.security.InternalAuth;
import com.example.demo.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/internal")
public class InternalEngagementController {
    private final NotificationService notifications;
    private final NotificationRepository notificationRepository;
    private final ReviewRepository reviewRepository;
    private final InternalAuth internalAuth;

    public InternalEngagementController(
            NotificationService notifications,
            NotificationRepository notificationRepository,
            ReviewRepository reviewRepository,
            InternalAuth internalAuth) {
        this.notifications = notifications;
        this.notificationRepository = notificationRepository;
        this.reviewRepository = reviewRepository;
        this.internalAuth = internalAuth;
    }

    @PostMapping("/notifications")
    void create(@RequestBody NotificationCreate request,
                @RequestHeader("X-Internal-Secret") String secret) {
        internalAuth.check(secret);
        notifications.create(request.userId(), request.title(), request.message());
    }

    @GetMapping("/engagement/stats")
    Map<String, Long> stats(@RequestHeader("X-Internal-Secret") String secret) {
        internalAuth.check(secret);
        return Map.of(
                "notifications", notificationRepository.count(),
                "reviews", reviewRepository.count());
    }

    public record NotificationCreate(Long userId, String title, String message) {
    }
}
