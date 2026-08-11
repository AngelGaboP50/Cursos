package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 40)
    private String level;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Course() {
    }

    public Course(String title, String description, String category, String level,
                  BigDecimal price, CourseStatus status, LocalDate startDate,
                  LocalDate endDate, String imageUrl) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.level = level;
        this.price = price;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }

    @PrePersist
    void initializeDates() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void updateDate() {
        updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getLevel() { return level; }
    public BigDecimal getPrice() { return price; }
    public CourseStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getImageUrl() { return imageUrl; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public boolean isPublished() { return status == CourseStatus.PUBLISHED; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setLevel(String level) { this.level = level; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStatus(CourseStatus status) { this.status = status; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
