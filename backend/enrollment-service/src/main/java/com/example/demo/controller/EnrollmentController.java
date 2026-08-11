package com.example.demo.controller;
import com.example.demo.dto.request.ProgressUpdateRequest;
import com.example.demo.dto.response.EnrollmentResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/enrollments") public class EnrollmentController  {
    private final EnrollmentService s;
    public EnrollmentController(EnrollmentService s) {
        this.s=s;
    }
    private UserPrincipal p(Authentication a) {
        return (UserPrincipal)a.getPrincipal();
    }
    @PostMapping("/courses/{courseId}") @ResponseStatus(HttpStatus.CREATED) EnrollmentResponse enroll(@PathVariable Long courseId,Authentication a) {
        return s.enroll(courseId,p(a));
    }
    @GetMapping("/me") List<EnrollmentResponse> mine(Authentication a) {
        return s.mine(p(a));
    }
    @DeleteMapping("/{id}") EnrollmentResponse cancel(@PathVariable Long id,Authentication a) {
        return s.cancel(id,p(a));
    }
    @PatchMapping("/{id}/progress") EnrollmentResponse progress(@PathVariable Long id,@Valid @RequestBody ProgressUpdateRequest r,Authentication a) {
        return s.progress(id,r.progressPercent(),p(a));
    }
}
