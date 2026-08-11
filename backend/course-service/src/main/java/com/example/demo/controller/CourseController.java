package com.example.demo.controller;
import com.example.demo.dto.response.CourseDTO;
import com.example.demo.service.CourseService;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/courses") public class CourseController  {
    private final CourseService s;
    public CourseController(CourseService s) {
        this.s=s;
    }
    @GetMapping List<CourseDTO> list(@RequestParam(defaultValue="")String query,@RequestParam(defaultValue="")String category) {
        return s.search(query,category);
    }
    @GetMapping("/categories") List<String> categories() {
        return s.categories();
    }
    @GetMapping("/{id}") CourseDTO detail(@PathVariable Long id) {
        return s.publicDetail(id);
    }
}
