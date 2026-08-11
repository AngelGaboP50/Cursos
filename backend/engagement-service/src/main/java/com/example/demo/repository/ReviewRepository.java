package com.example.demo.repository;

import com.example.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserIdAndCourseId(Long userId, Long courseId);
    List<Review> findByCourseIdOrderByUpdatedAtDesc(Long courseId);

    @Query("select coalesce(avg(r.rating), 0) from Review r where r.courseId = :courseId")
    double averageForCourse(@Param("courseId") Long courseId);
}
