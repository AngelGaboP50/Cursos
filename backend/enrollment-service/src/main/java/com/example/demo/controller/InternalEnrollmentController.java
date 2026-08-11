package com.example.demo.controller;
import com.example.demo.model.EnrollmentStatus;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.security.InternalAuth;
import com.example.demo.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
@RestController @RequestMapping("/internal/enrollments") public class InternalEnrollmentController  {
    private final EnrollmentRepository r;
    private final EnrollmentService s;
    private final InternalAuth a;
    public InternalEnrollmentController(EnrollmentRepository r,EnrollmentService s,InternalAuth a) {
        this.r=r;
        this.s=s;
        this.a=a;
    }
    @GetMapping("/access") Map<String,Boolean> access(@RequestParam Long userId,@RequestParam Long courseId,@RequestHeader("X-Internal-Secret")String x) {
        a.check(x);
        return Map.of("access",s.hasAccess(userId,courseId));
    }
    @GetMapping("/stats") Map<String,Long> stats(@RequestHeader("X-Internal-Secret")String x) {
        a.check(x);
        return Map.of("enrollments",r.count(),"activeEnrollments",r.countByStatus(EnrollmentStatus.ACTIVE),"completedEnrollments",r.countByStatus(EnrollmentStatus.COMPLETED));
    }
    @GetMapping(value="/export",produces="text/csv") byte[] export(@RequestHeader("X-Internal-Secret")String x) {
        a.check(x);
        StringBuilder b=new StringBuilder("id,usuario,curso,estado,progreso,fecha\n");
        r.findAll().forEach(e->b.append(e.getId()).append(',').append(cell(e.getUserEmail())).append(',').append(cell(e.course().title())).append(',').append(e.getStatus()).append(',').append(e.getProgressPercent()).append(',').append(e.getEnrollmentDate()).append('\n'));
        return ("\uFEFF"+b).getBytes(StandardCharsets.UTF_8);
    }
    private String cell(String v) {
        return "\""+v.replace("\"","\"\"")+"\"";
    }
}
