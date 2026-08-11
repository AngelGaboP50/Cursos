package com.example.demo.service;
import com.example.demo.dto.response.CourseDTO;
import com.example.demo.exception.*;
import com.example.demo.model.Favorite;
import com.example.demo.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service public class FavoriteService {
    private final FavoriteRepository r;
    private final CourseClient courses;
    public FavoriteService(FavoriteRepository r,CourseClient courses) {
        this.r=r;
        this.courses=courses;
    }
    @Transactional public CourseDTO add(Long cid,Long uid) {
        if(r.findByUserIdAndCourseId(uid,cid).isPresent())throw new ConflictException("El curso ya está en favoritos");
        CourseDTO c=courses.require(cid);
        if(!"PUBLISHED".equals(c.status()))throw new ResourceNotFoundException("Curso no encontrado");
        r.save(new Favorite(uid,c));
        return c;
    }
    @Transactional(readOnly=true)public List<CourseDTO> mine(Long uid) {
        return r.findByUserIdOrderByCreatedAtDesc(uid).stream().map(Favorite::course).toList();
    }
    @Transactional public void remove(Long cid,Long uid) {
        Favorite f=r.findByUserIdAndCourseId(uid,cid).orElseThrow(()->new ResourceNotFoundException("Favorito no encontrado"));
        r.delete(f);
    }
}
