package com.example.demo.controller;
import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController public class ReviewController {
    private final ReviewService s;
    public ReviewController(ReviewService s) {
        this.s=s;
    }
    @GetMapping("/api/courses/{courseId}/reviews")java.util.Map<String,Object> list(@PathVariable Long courseId) {
        return s.forCourse(courseId);
    }
    @PutMapping("/api/reviews/courses/{courseId}")ReviewResponse save(@PathVariable Long courseId,@Valid @RequestBody ReviewRequest r,Authentication a) {
        return s.save(courseId,r,(UserPrincipal)a.getPrincipal());
    }
    @DeleteMapping("/api/reviews/courses/{courseId}")ResponseEntity<Void> del(@PathVariable Long courseId,Authentication a) {
        s.delete(courseId,((UserPrincipal)a.getPrincipal()).userId());
        return ResponseEntity.noContent().build();
    }
}
