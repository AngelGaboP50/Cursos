package com.example.demo.dto.response; import java.math.BigDecimal; import java.time.LocalDate;
public record CourseDTO(Long id,String title,String description,String category,String level,BigDecimal price,String status,LocalDate startDate,LocalDate endDate,String imageUrl){}
