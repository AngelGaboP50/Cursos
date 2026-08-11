package com.example.demo.controller;
import com.example.demo.dto.response.NotificationResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.NotificationService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/notifications") public class NotificationController {
    private final NotificationService s;
    public NotificationController(NotificationService s) {
        this.s=s;
    }
    private Long u(Authentication a) {
        return ((UserPrincipal)a.getPrincipal()).userId();
    }
    @GetMapping List<NotificationResponse> list(Authentication a) {
        return s.list(u(a));
    }
    @GetMapping("/unread-count")Map<String,Long> unread(Authentication a) {
        return Map.of("count",s.unread(u(a)));
    }
    @PatchMapping("/{id}/read")NotificationResponse read(@PathVariable Long id,Authentication a) {
        return s.read(id,u(a));
    }
    @PatchMapping("/read-all")ResponseEntity<Void> all(Authentication a) {
        s.readAll(u(a));
        return ResponseEntity.noContent().build();
    }
}
