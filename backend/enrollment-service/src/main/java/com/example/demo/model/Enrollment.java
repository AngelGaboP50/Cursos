package com.example.demo.model;
import com.example.demo.dto.response.CourseDTO;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
@Entity @Table(name="enrollments",uniqueConstraints=@UniqueConstraint(name="uk_enrollments_user_course",columnNames= {
    "user_id","course_id"
}
)) public class Enrollment  {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="user_id",nullable=false) private Long userId;
    @Column(name="user_email",nullable=false,length=255) private String userEmail;
    @Column(name="course_id",nullable=false) private Long courseId;
    @Column(name="course_title",nullable=false,length=160) private String courseTitle;
    @Column(name="course_description",nullable=false,length=2000) private String courseDescription;
    @Column(name="course_category",nullable=false,length=100) private String courseCategory;
    @Column(name="course_level",nullable=false,length=40) private String courseLevel;
    @Column(name="course_price",nullable=false,precision=12,scale=2) private BigDecimal coursePrice;
    @Column(name="course_status",nullable=false,length=20) private String courseStatus;
    @Column(name="course_start_date") private LocalDate courseStartDate;
    @Column(name="course_end_date") private LocalDate courseEndDate;
    @Column(name="course_image_url",length=500) private String courseImageUrl;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private EnrollmentStatus status;
    @Column(name="progress_percent",nullable=false) private int progressPercent;
    @Column(name="enrollment_date",nullable=false) private Instant enrollmentDate;
    @Column(name="updated_at",nullable=false) private Instant updatedAt;
    protected Enrollment() {
    }
    public Enrollment(Long userId,String email,CourseDTO c) {
        this.userId=userId;
        this.userEmail=email;
        refresh(c);
        this.status=EnrollmentStatus.ACTIVE;
        this.progressPercent=0;
    }
    @PrePersist void p() {
        Instant n=Instant.now();
        if(enrollmentDate==null)enrollmentDate=n;
        updatedAt=n;
    }
    @PreUpdate void u() {
        updatedAt=Instant.now();
    }
    public void refresh(CourseDTO c) {
        courseId=c.id();
        courseTitle=c.title();
        courseDescription=c.description();
        courseCategory=c.category();
        courseLevel=c.level();
        coursePrice=c.price();
        courseStatus=c.status();
        courseStartDate=c.startDate();
        courseEndDate=c.endDate();
        courseImageUrl=c.imageUrl();
    }
    public CourseDTO course() {
        return new CourseDTO(courseId,courseTitle,courseDescription,courseCategory,courseLevel,coursePrice,courseStatus,courseStartDate,courseEndDate,courseImageUrl);
    }
    public void reactivate(CourseDTO c) {
        refresh(c);
        status=EnrollmentStatus.ACTIVE;
        progressPercent=0;
        enrollmentDate=Instant.now();
    }
    public void cancel() {
        status=EnrollmentStatus.CANCELLED;
    }
    public void updateProgress(int p) {
        progressPercent=p;
        status=p==100?EnrollmentStatus.COMPLETED:EnrollmentStatus.ACTIVE;
    }
    public Long getId() {
        return id;
    }
    public Long getUserId() {
        return userId;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public Long getCourseId() {
        return courseId;
    }
    public EnrollmentStatus getStatus() {
        return status;
    }
    public int getProgressPercent() {
        return progressPercent;
    }
    public Instant getEnrollmentDate() {
        return enrollmentDate;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
