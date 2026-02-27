package com.hotpot.service;

import java.util.List;

import com.hotpot.dto.LoginRequest;
import com.hotpot.dto.LoginResponse;
import com.hotpot.dto.RegisterRequest;
import com.hotpot.entity.User;

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
