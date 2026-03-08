package com.wipro.hotpot.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wipro.hotpot.dto.LoginRequest;
import com.wipro.hotpot.dto.LoginResponse;
import com.wipro.hotpot.dto.RegisterRequest;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.service.IAuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private IAuthService authService;

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User saved = authService.registerUser(request);
            
            LoginResponse resp = new LoginResponse();
            resp.setId(saved.getId());
            resp.setName(saved.getName());
            resp.setEmail(saved.getEmail());
            resp.setRole("ROLE_" + saved.getRole().name());
            resp.setMessage("Registration successful!");
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.loginUser(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }
}