package com.example.demo.dto.response;

public record ReportSummary(long users, long courses, long publishedCourses, long enrollments,
                            long activeEnrollments, long completedEnrollments, long notifications,
                            long reviews) { }
