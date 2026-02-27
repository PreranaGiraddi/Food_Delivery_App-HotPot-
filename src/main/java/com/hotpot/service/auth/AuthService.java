package com.hotpot.service.auth;

import com.hotpot.dto.auth.LoginRequest;
import com.hotpot.dto.auth.LoginResponse;
import com.hotpot.dto.auth.RegisterRequest;
import com.hotpot.entity.auth.User;
import java.util.List;

public interface AuthService {

   
    User registerUser(RegisterRequest request);

    
    LoginResponse loginUser(LoginRequest request);

    
    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User updateUser(Long id, RegisterRequest request);

    void deleteUser(Long id);

    boolean emailExists(String email);
}
