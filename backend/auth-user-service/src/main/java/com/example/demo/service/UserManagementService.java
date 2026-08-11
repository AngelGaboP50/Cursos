package com.example.demo.service;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.exception.*;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service public class UserManagementService  {
    private final UserRepository repo;
    private final AuditClient audit;
    public UserManagementService(UserRepository repo,AuditClient audit) {
        this.repo=repo;
        this.audit=audit;
    }
    @Transactional(readOnly=true) public List<UserResponse> list() {
        return repo.findAllByOrderByCreatedAtDesc().stream().map(UserResponse::from).toList();
    }
    @Transactional public UserResponse setEnabled(Long id,boolean enabled,Long actorId,String actorEmail) {
        User t=repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Usuario no encontrado"));
        if(t.getId().equals(actorId)&&!enabled)throw new BusinessRuleException("No puedes desactivar tu propia cuenta administradora");
        t.setEnabled(enabled);
        audit.record(actorId,actorEmail,enabled?"USER_ENABLED":"USER_DISABLED","USER",t.getId(),t.getEmail());
        return UserResponse.from(t);
    }
}
