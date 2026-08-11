package com.example.demo.controller;
import com.example.demo.dto.response.CourseDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CourseStatus;
import com.example.demo.repository.CourseRepository;
import com.example.demo.security.InternalAuth;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
@RestController @RequestMapping("/internal/courses") public class InternalCourseController  {
    private final CourseRepository r;
    private final InternalAuth a;
    public InternalCourseController(CourseRepository r,InternalAuth a) {
        this.r=r;
        this.a=a;
    }
    @GetMapping("/{id}") CourseDTO one(@PathVariable Long id,@RequestHeader("X-Internal-Secret")String s) {
        a.check(s);
        return r.findById(id).map(CourseDTO::from).orElseThrow(()->new ResourceNotFoundException("Curso no encontrado"));
    }
    @GetMapping("/stats") Map<String,Long> stats(@RequestHeader("X-Internal-Secret")String s) {
        a.check(s);
        return Map.of("courses",r.count(),"publishedCourses",r.countByStatus(CourseStatus.PUBLISHED));
    }
    @GetMapping(value="/export",produces="text/csv") byte[] export(@RequestHeader("X-Internal-Secret")String s) {
        a.check(s);
        StringBuilder x=new StringBuilder("id,titulo,categoria,nivel,precio,estado\n");
        r.findAllByOrderByCreatedAtDesc().forEach(c->x.append(c.getId()).append(',').append(cell(c.getTitle())).append(',').append(cell(c.getCategory())).append(',').append(cell(c.getLevel())).append(',').append(c.getPrice()).append(',').append(c.getStatus()).append('\n'));
        return ("\uFEFF"+x).getBytes(StandardCharsets.UTF_8);
    }
    private String cell(String v) {
        return "\""+v.replace("\"","\"\"")+"\"";
    }
}
