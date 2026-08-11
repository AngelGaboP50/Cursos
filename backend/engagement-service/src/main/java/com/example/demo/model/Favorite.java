package com.example.demo.model;
import com.example.demo.dto.response.CourseDTO;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
@Entity @Table(name="favorites",uniqueConstraints=@UniqueConstraint(name="uk_favorites_user_course",columnNames= {
    "user_id","course_id"
}
)) public class Favorite {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)private Long id;
    @Column(name="user_id",nullable=false)private Long userId;
    @Column(name="course_id",nullable=false)private Long courseId;
    @Column(name="course_title",nullable=false,length=160)private String title;
    @Column(name="course_description",nullable=false,length=2000)private String description;
    @Column(name="course_category",nullable=false,length=100)private String category;
    @Column(name="course_level",nullable=false,length=40)private String level;
    @Column(name="course_price",nullable=false,precision=12,scale=2)private BigDecimal price;
    @Column(name="course_status",nullable=false,length=20)private String status;
    @Column(name="course_start_date")private LocalDate startDate;
    @Column(name="course_end_date")private LocalDate endDate;
    @Column(name="course_image_url",length=500)private String imageUrl;
    @Column(name="created_at",nullable=false,updatable=false)private Instant createdAt;
    protected Favorite() {
    }
    public Favorite(Long uid,CourseDTO c) {
        userId=uid;
        courseId=c.id();
        title=c.title();
        description=c.description();
        category=c.category();
        level=c.level();
        price=c.price();
        status=c.status();
        startDate=c.startDate();
        endDate=c.endDate();
        imageUrl=c.imageUrl();
    }
    @PrePersist void p() {
        if(createdAt==null)createdAt=Instant.now();
    }
    public Long getId() {
        return id;
    }
    public Long getUserId() {
        return userId;
    }
    public Long getCourseId() {
        return courseId;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public CourseDTO course() {
        return new CourseDTO(courseId,title,description,category,level,price,status,startDate,endDate,imageUrl);
    }
}
