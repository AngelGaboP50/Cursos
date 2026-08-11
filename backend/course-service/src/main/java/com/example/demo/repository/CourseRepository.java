package com.example.demo.repository;

import com.example.demo.model.Course;
import com.example.demo.model.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
            select c from Course c
            where c.status = com.example.demo.model.CourseStatus.PUBLISHED
              and (:query = '' or lower(c.title) like lower(concat('%', :query, '%'))
                   or lower(c.description) like lower(concat('%', :query, '%')))
              and (:category = '' or lower(c.category) = lower(:category))
            order by c.title
            """)
    List<Course> searchPublished(@Param("query") String query, @Param("category") String category);

    List<Course> findAllByOrderByCreatedAtDesc();
    List<Course> findByStatusOrderByTitle(CourseStatus status);
    long countByStatus(CourseStatus status);

    @Query("select distinct c.category from Course c where c.status = com.example.demo.model.CourseStatus.PUBLISHED order by c.category")
    List<String> findPublishedCategories();
}
