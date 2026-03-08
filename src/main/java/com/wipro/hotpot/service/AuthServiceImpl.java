package com.wipro.hotpot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.hotpot.config.JwtUtil;
import com.wipro.hotpot.dto.LoginRequest;
import com.wipro.hotpot.dto.LoginResponse;
import com.wipro.hotpot.dto.RegisterRequest;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.repository.IUserRepository;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ─── REGISTER ────────────────────────────────────────────────────────────
    @Override
    public User registerUser(RegisterRequest request) {

        if (userRepository.isEmailExists(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        // Strip ROLE_ prefix if present, default to USER
        User.Role role = User.Role.USER;
        if (request.getRole() != null && !request.getRole().isBlank()) {
            String raw = request.getRole().toUpperCase().replace("ROLE_", "");
            try {
                role = User.Role.valueOf(raw);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role: " + request.getRole()
                    + ". Must be USER, RESTAURANT, or ADMIN.");
            }
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setContactNumber(request.getContactNumber());
        user.setAddress(request.getAddress());
        user.setGender(request.getGender());
        user.setRole(role);
        user.setActive(true);

        return userRepository.save(user);
    }

    // ─── LOGIN ───────────────────────────────────────────────────────────────
    @Override
    public LoginResponse loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(
                    "No account found with email: " + request.getEmail()));

        if (!user.isActive()) {
            throw new RuntimeException("Account deactivated. Contact admin.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password!");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setToken(token);
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole("ROLE_" + user.getRole().name());
        response.setMessage("Login successful!");
        return response;
    }

    // ─── GET BY ID ───────────────────────────────────────────────────────────
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    // ─── GET BY EMAIL ────────────────────────────────────────────────────────
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    // ─── GET ALL ─────────────────────────────────────────────────────────────
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────
    @Override
    public User updateUser(Long id, RegisterRequest request) {
        User user = getUserById(id);
        if (request.getName() != null)          user.setName(request.getName());
        if (request.getContactNumber() != null) user.setContactNumber(request.getContactNumber());
        if (request.getAddress() != null)       user.setAddress(request.getAddress());
        if (request.getGender() != null)        user.setGender(request.getGender());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null && !request.getRole().isBlank()) {
            String raw = request.getRole().toUpperCase().replace("ROLE_", "");
            user.setRole(User.Role.valueOf(raw));
        }
        return userRepository.save(user);
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    // ─── EMAIL EXISTS ────────────────────────────────────────────────────────
    @Override
    public boolean isEmailExists(String email) {
        return userRepository.isEmailExists(email);
    }
}