package com.example.demo.client;

import com.example.demo.dto.response.CourseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class CourseServiceClient {

    private final RestTemplate restTemplate;

    @Value("${course-service.url}")
    private String courseServiceUrl;

    public CourseServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CourseDTO> getAllCourses() {
        try {
            CourseDTO[] courses = restTemplate.getForObject(courseServiceUrl, CourseDTO[].class);
            return courses != null ? Arrays.asList(courses) : List.of();
        } catch (Exception e) {
            // Fallback mientras el microservicio de Course no esté disponible
            System.out.println("No se pudo conectar a course-service, usando datos de prueba: " + e.getMessage());
            return List.of(
                    mockCourse(1L, "Matemáticas I", "Curso básico de álgebra", "Prof. García", 5),
                    mockCourse(2L, "Programación I", "Introducción a Java", "Prof. López", 6)
            );
        }
    }

    private CourseDTO mockCourse(Long id, String name, String desc, String teacher, int credits) {
        CourseDTO c = new CourseDTO();
        c.setId(id);
        c.setName(name);
        c.setDescription(desc);
        c.setTeacherName(teacher);
        c.setCredits(credits);
        return c;
    }
}