package com.example.demo.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="reviews",uniqueConstraints=@UniqueConstraint(name="uk_reviews_user_course",columnNames= {
    "user_id","course_id"
}
)) public class Review {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)private Long id;
    @Column(name="user_id",nullable=false)private Long userId;
    @Column(name="user_name",nullable=false,length=160)private String userName;
    @Column(name="course_id",nullable=false)private Long courseId;
    @Column(nullable=false)private int rating;
    @Column(nullable=false,length=1200)private String comment;
    @Column(name="created_at",nullable=false,updatable=false)private Instant createdAt;
    @Column(name="updated_at",nullable=false)private Instant updatedAt;
    protected Review() {
    }
    public Review(Long uid,String name,Long cid,int rating,String comment) {
        userId=uid;
        userName=name;
        courseId=cid;
        this.rating=rating;
        this.comment=comment;
    }
    @PrePersist void p() {
        Instant n=Instant.now();
        if(createdAt==null)createdAt=n;
        updatedAt=n;
    }
    @PreUpdate void u() {
        updatedAt=Instant.now();
    }
    public void update(String name,int rating,String comment) {
        userName=name;
        this.rating=rating;
        this.comment=comment;
    }
    public Long getId() {
        return id;
    }
    public Long getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public Long getCourseId() {
        return courseId;
    }
    public int getRating() {
        return rating;
    }
    public String getComment() {
        return comment;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
