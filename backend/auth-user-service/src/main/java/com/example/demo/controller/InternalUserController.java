package com.example.demo.controller;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.InternalAuth;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/internal/users") public class InternalUserController  {
    private final UserRepository r;
    private final InternalAuth auth;
    public InternalUserController(UserRepository r,InternalAuth auth) {
        this.r=r;
        this.auth=auth;
    }
    @GetMapping("/{id}") UserResponse one(@PathVariable Long id,@RequestHeader("X-Internal-Secret")String s) {
        auth.check(s);
        return r.findById(id).map(UserResponse::from).orElseThrow(()->new ResourceNotFoundException("Usuario no encontrado"));
    }
    @GetMapping("/stats") Map<String,Long> stats(@RequestHeader("X-Internal-Secret")String s) {
        auth.check(s);
        return Map.of("users",r.count());
    }
}
