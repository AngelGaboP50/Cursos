package com.example.demo.dto.response;

import com.example.demo.model.Course;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CourseDTO(
        Long id,
        String title,
        String description,
        String category,
        String level,
        BigDecimal price,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        String imageUrl
) {
    public static CourseDTO from(Course course) {
        return new CourseDTO(course.getId(), course.getTitle(), course.getDescription(),
                course.getCategory(), course.getLevel(), course.getPrice(), course.getStatus().name(),
                course.getStartDate(), course.getEndDate(), course.getImageUrl());
    }
}
