package com.example.demo.controller;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.UserManagementService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/admin/users") public class AdminUserController  {
    private final UserManagementService s;
    public AdminUserController(UserManagementService s) {
        this.s=s;
    }
    @GetMapping List<UserResponse> list() {
        return s.list();
    }
    @PatchMapping("/{id}/enabled") UserResponse enabled(@PathVariable Long id,@RequestParam boolean value,Authentication a) {
        UserPrincipal p=(UserPrincipal)a.getPrincipal();
        return s.setEnabled(id,value,p.userId(),p.email());
    }
}
