package com.example.demo.dto.response;
import com.example.demo.model.Review;
import java.time.Instant;
public record ReviewResponse(Long id,Long userId,String userName,int rating,String comment,Instant updatedAt) {
    public static ReviewResponse from(Review r) {
        return new ReviewResponse(r.getId(),r.getUserId(),r.getUserName(),r.getRating(),r.getComment(),r.getUpdatedAt());
    }
}
