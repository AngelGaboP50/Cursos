package com.example.demo.service;
import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service public class ReviewService {
    private final ReviewRepository r;
    private final EnrollmentClient enrollments;
    public ReviewService(ReviewRepository r,EnrollmentClient e) {
        this.r=r;
        this.enrollments=e;
    }
    @Transactional(readOnly=true)public Map<String,Object> forCourse(Long cid) {
        List<ReviewResponse>x=r.findByCourseIdOrderByUpdatedAtDesc(cid).stream().map(ReviewResponse::from).toList();
        return Map.of("average",r.averageForCourse(cid),"count",x.size(),"reviews",x);
    }
    @Transactional public ReviewResponse save(Long cid,ReviewRequest req,UserPrincipal p) {
        if(!enrollments.hasAccess(p.userId(),cid))throw new BusinessRuleException("Debes estar inscrito para reseñar este curso");
        Review x=r.findByUserIdAndCourseId(p.userId(),cid).orElseGet(()->new Review(p.userId(),p.displayName(),cid,req.rating(),req.comment().trim()));
        x.update(p.displayName(),req.rating(),req.comment().trim());
        return ReviewResponse.from(r.save(x));
    }
    @Transactional public void delete(Long cid,Long uid) {
        Review x=r.findByUserIdAndCourseId(uid,cid).orElseThrow(()->new com.example.demo.exception.ResourceNotFoundException("Reseña no encontrada"));
        r.delete(x);
    }
}
