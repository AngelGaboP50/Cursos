package com.example.demo.controller;
import com.example.demo.dto.request.CourseCreateRequest;
import com.example.demo.dto.response.CourseDTO;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/admin/courses") public class AdminCourseController  {
    private final CourseService s;
    public AdminCourseController(CourseService s) {
        this.s=s;
    }
    private UserPrincipal p(Authentication a) {
        return (UserPrincipal)a.getPrincipal();
    }
    @GetMapping List<CourseDTO> list() {
        return s.adminList();
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) CourseDTO create(@Valid @RequestBody CourseCreateRequest x,Authentication a) {
        return s.create(x,p(a));
    }
    @PutMapping("/{id}") CourseDTO update(@PathVariable Long id,@Valid @RequestBody CourseCreateRequest x,Authentication a) {
        return s.update(id,x,p(a));
    }
    @DeleteMapping("/{id}") CourseDTO off(@PathVariable Long id,Authentication a) {
        return s.deactivate(id,p(a));
    }
}
