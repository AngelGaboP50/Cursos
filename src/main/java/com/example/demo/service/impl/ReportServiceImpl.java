package com.example.demo.service.impl;

import com.example.demo.client.CourseServiceClient;
import com.example.demo.dto.response.CourseDTO;
import com.example.demo.service.ReportService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final CourseServiceClient courseServiceClient;

    public ReportServiceImpl(CourseServiceClient courseServiceClient) {
        this.courseServiceClient = courseServiceClient;
    }

    @Override
    public byte[] generateCoursesCsv() {
        List<CourseDTO> courses = courseServiceClient.getAllCourses();

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Nombre,Descripcion,Profesor,Creditos\n");

        for (CourseDTO course : courses) {
            csv.append(course.getId()).append(",")
               .append(escapeCsv(course.getName())).append(",")
               .append(escapeCsv(course.getDescription())).append(",")
               .append(escapeCsv(course.getTeacherName())).append(",")
               .append(course.getCredits()).append("\n");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(csv.toString().getBytes(StandardCharsets.UTF_8));
        return out.toByteArray();
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}