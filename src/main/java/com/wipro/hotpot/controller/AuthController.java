package com.wipro.hotpot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.wipro.hotpot.entity.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.hotpot.dto.LoginRequest;
import com.wipro.hotpot.dto.LoginResponse;
import com.wipro.hotpot.dto.RegisterRequest;
import com.wipro.hotpot.service.IAuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

  
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterRequest request) {
        User user = authService.registerUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.loginUser(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

   
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = authService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    
    @GetMapping("/user/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = authService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

   
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    
    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @Valid @RequestBody RegisterRequest request) {
        User user = authService.updateUser(id, request);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }

    
    @GetMapping("/isEmailExists/{email}")
    public ResponseEntity<Boolean> isEmailExists(@PathVariable String email) {
        boolean exists = authService.isEmailExists(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}
