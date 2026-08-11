package com.example.demo.repository;
import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    Optional<Enrollment> findByUserIdAndCourseId(Long u,Long c);
    Optional<Enrollment> findByIdAndUserId(Long id,Long u);
    List<Enrollment> findByUserIdOrderByEnrollmentDateDesc(Long u);
    boolean existsByUserIdAndCourseIdAndStatusNot(Long u,Long c,EnrollmentStatus s);
    long countByStatus(EnrollmentStatus s);
}
