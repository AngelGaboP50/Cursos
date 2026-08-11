package com.example.demo.controller;

import com.example.demo.dto.response.ReportSummary;
import com.example.demo.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    ReportSummary summary() {
        return reportService.summary();
    }

    @GetMapping(value = "/courses.csv", produces = "text/csv")
    ResponseEntity<byte[]> courses() {
        return csv("cursos.csv", reportService.coursesCsv());
    }

    @GetMapping(value = "/enrollments.csv", produces = "text/csv")
    ResponseEntity<byte[]> enrollments() {
        return csv("inscripciones.csv", reportService.enrollmentsCsv());
    }

    private ResponseEntity<byte[]> csv(String filename, byte[] body) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(body);
    }
}
