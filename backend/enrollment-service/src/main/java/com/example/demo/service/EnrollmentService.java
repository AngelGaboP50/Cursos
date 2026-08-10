package com.example.demo.service;
import com.example.demo.dto.response.*;
import com.example.demo.exception.*;
import com.example.demo.model.*;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service public class EnrollmentService  {
    private final EnrollmentRepository r;
    private final CourseClient courses;
    private final EngagementClient engagement;
    private final AuditClient audit;
    public EnrollmentService(EnrollmentRepository r,CourseClient courses,EngagementClient engagement,AuditClient audit) {
        this.r=r;
        this.courses=courses;
        this.engagement=engagement;
        this.audit=audit;
    }
    @Transactional public EnrollmentResponse enroll(Long cid,UserPrincipal p) {
        CourseDTO c=courses.require(cid);
        if(!"PUBLISHED".equals(c.status()))throw new BusinessRuleException("El curso no está disponible para inscripción");
        Enrollment e=r.findByUserIdAndCourseId(p.userId(),cid).orElse(null);
        if(e!=null&&e.getStatus()!=EnrollmentStatus.CANCELLED)throw new ConflictException("Ya estás inscrito en este curso");
        if(e==null)e=new Enrollment(p.userId(),p.email(),c);
        else e.reactivate(c);
        e=r.save(e);
        engagement.notify(p.userId(),"Inscripción confirmada","Te inscribiste en "+c.title());
        audit.record(p.userId(),p.email(),"ENROLLMENT_CREATED","ENROLLMENT",e.getId(),c.title());
        return EnrollmentResponse.from(e);
    }
    @Transactional(readOnly=true) public List<EnrollmentResponse> mine(UserPrincipal p) {
        return r.findByUserIdOrderByEnrollmentDateDesc(p.userId()).stream().map(EnrollmentResponse::from).toList();
    }
    @Transactional public EnrollmentResponse cancel(Long id,UserPrincipal p) {
        Enrollment e=owned(id,p.userId());
        if(e.getStatus()==EnrollmentStatus.CANCELLED)throw new ConflictException("La inscripción ya está cancelada");
        e.cancel();
        audit.record(p.userId(),p.email(),"ENROLLMENT_CANCELLED","ENROLLMENT",e.getId(),e.course().title());
        return EnrollmentResponse.from(e);
    }
    @Transactional public EnrollmentResponse progress(Long id,int x,UserPrincipal p) {
        Enrollment e=owned(id,p.userId());
        if(e.getStatus()==EnrollmentStatus.CANCELLED)throw new BusinessRuleException("No puedes actualizar una inscripción cancelada");
        boolean completed=x==100&&e.getStatus()!=EnrollmentStatus.COMPLETED;
        e.updateProgress(x);
        if(completed)engagement.notify(p.userId(),"Curso completado","Completaste "+e.course().title());
        audit.record(p.userId(),p.email(),"PROGRESS_UPDATED","ENROLLMENT",e.getId(),"Progreso: "+x+"%");
        return EnrollmentResponse.from(e);
    }
    public boolean hasAccess(Long uid,Long cid) {
        return r.existsByUserIdAndCourseIdAndStatusNot(uid,cid,EnrollmentStatus.CANCELLED);
    }
    private Enrollment owned(Long id,Long uid) {
        return r.findByIdAndUserId(id,uid).orElseThrow(()->new ResourceNotFoundException("Inscripción no encontrada"));
    }
}
