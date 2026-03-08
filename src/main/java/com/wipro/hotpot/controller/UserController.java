package com.wipro.hotpot.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.wipro.hotpot.entity.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.hotpot.repository.IUserRepository;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    // GET /api/user/all  — admin only (secured by JWT in SecurityConfig)
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // GET /api/user/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/user/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    // PUT /api/user/toggle/{id}?active=true/false  — activate/deactivate
    @PutMapping("/toggle/{id}")
    public ResponseEntity<?> toggleUser(@PathVariable Long id, @RequestParam boolean active) {
        return userRepository.findById(id).map(user -> {
            user.setActive(active);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "User " + (active ? "activated" : "deactivated")));
        }).orElse(ResponseEntity.notFound().build());
    }
}