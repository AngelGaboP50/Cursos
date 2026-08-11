package com.example.demo.controller;
import com.example.demo.dto.response.CourseDTO;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.FavoriteService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/favorites") public class FavoriteController {
    private final FavoriteService s;
    public FavoriteController(FavoriteService s) {
        this.s=s;
    }
    private Long u(Authentication a) {
        return ((UserPrincipal)a.getPrincipal()).userId();
    }
    @GetMapping List<CourseDTO> list(Authentication a) {
        return s.mine(u(a));
    }
    @PostMapping("/{courseId}")@ResponseStatus(HttpStatus.CREATED)CourseDTO add(@PathVariable Long courseId,Authentication a) {
        return s.add(courseId,u(a));
    }
    @DeleteMapping("/{courseId}")ResponseEntity<Void> del(@PathVariable Long courseId,Authentication a) {
        s.remove(courseId,u(a));
        return ResponseEntity.noContent().build();
    }
}
