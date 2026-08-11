package com.example.demo.dto.request;

import com.example.demo.model.CourseStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CourseCreateRequest(
        @NotBlank @Size(max = 160) String title,
        @NotBlank @Size(max = 2000) String description,
        @NotBlank @Size(max = 100) String category,
        @NotBlank @Size(max = 40) String level,
        @NotNull @DecimalMin("0.00") BigDecimal price,
        @NotNull CourseStatus status,
        LocalDate startDate,
        LocalDate endDate,
        @Size(max = 500) String imageUrl
) { }
