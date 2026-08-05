package com.example.demo.controller;

import com.example.demo.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/courses/export")
    public ResponseEntity<byte[]> exportCourses() {
        byte[] csvData = reportService.generateCoursesCsv();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cursos.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }
}