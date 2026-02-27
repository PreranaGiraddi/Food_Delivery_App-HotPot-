package com.wipro.hotpot.service;

import java.util.List;

import com.wipro.hotpot.dto.LoginRequest;
import com.wipro.hotpot.dto.LoginResponse;
import com.wipro.hotpot.dto.RegisterRequest;
import com.wipro.hotpot.entity.User;

public interface IAuthService {

	User registerUser(RegisterRequest request);

	LoginResponse loginUser(LoginRequest request);

	User getUserById(Long id);

	User getUserByEmail(String email);

	List<User> getAllUsers();

	User updateUser(Long id, RegisterRequest request);

	void deleteUser(Long id);

	boolean emailExists(String email);
}
