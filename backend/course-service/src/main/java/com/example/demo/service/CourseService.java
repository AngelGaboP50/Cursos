package com.example.demo.service;
import com.example.demo.dto.request.CourseCreateRequest;
import com.example.demo.dto.response.CourseDTO;
import com.example.demo.exception.*;
import com.example.demo.model.*;
import com.example.demo.repository.CourseRepository;
import com.example.demo.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service public class CourseService  {
    private final CourseRepository r;
    private final AuditClient audit;
    public CourseService(CourseRepository r,AuditClient audit) {
        this.r=r;
        this.audit=audit;
    }
    @Transactional(readOnly=true) public List<CourseDTO> search(String q,String cat) {
        return r.searchPublished(clean(q),clean(cat)).stream().map(CourseDTO::from).toList();
    }
    @Transactional(readOnly=true) public List<String> categories() {
        return r.findPublishedCategories();
    }
    @Transactional(readOnly=true) public CourseDTO publicDetail(Long id) {
        Course c=require(id);
        if(!c.isPublished())throw new ResourceNotFoundException("Curso no encontrado");
        return CourseDTO.from(c);
    }
    @Transactional(readOnly=true) public List<CourseDTO> adminList() {
        return r.findAllByOrderByCreatedAtDesc().stream().map(CourseDTO::from).toList();
    }
    @Transactional public CourseDTO create(CourseCreateRequest x,UserPrincipal p) {
        dates(x);
        Course c=new Course(x.title().trim(),x.description().trim(),x.category().trim(),x.level().trim(),x.price(),x.status(),x.startDate(),x.endDate(),nullable(x.imageUrl()));
        c=r.save(c);
        audit.record(p.userId(),p.email(),"COURSE_CREATED","COURSE",c.getId(),c.getTitle());
        return CourseDTO.from(c);
    }
    @Transactional public CourseDTO update(Long id,CourseCreateRequest x,UserPrincipal p) {
        dates(x);
        Course c=require(id);
        c.setTitle(x.title().trim());
        c.setDescription(x.description().trim());
        c.setCategory(x.category().trim());
        c.setLevel(x.level().trim());
        c.setPrice(x.price());
        c.setStatus(x.status());
        c.setStartDate(x.startDate());
        c.setEndDate(x.endDate());
        c.setImageUrl(nullable(x.imageUrl()));
        audit.record(p.userId(),p.email(),"COURSE_UPDATED","COURSE",c.getId(),c.getTitle());
        return CourseDTO.from(c);
    }
    @Transactional public CourseDTO deactivate(Long id,UserPrincipal p) {
        Course c=require(id);
        c.setStatus(CourseStatus.INACTIVE);
        audit.record(p.userId(),p.email(),"COURSE_DEACTIVATED","COURSE",c.getId(),c.getTitle());
        return CourseDTO.from(c);
    }
    public Course require(Long id) {
        return r.findById(id).orElseThrow(()->new ResourceNotFoundException("Curso no encontrado"));
    }
    private void dates(CourseCreateRequest x) {
        if(x.startDate()!=null&&x.endDate()!=null&&x.endDate().isBefore(x.startDate()))throw new BusinessRuleException("La fecha final no puede ser anterior a la inicial");
    }
    private String clean(String x) {
        return x==null?"":x.trim();
    }
    private String nullable(String x) {
        return x==null||x.isBlank()?null:x.trim();
    }
}
