package com.wipro.hotpot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Override
	public User registerUser(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already registered!");
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword())); // encrypt password
		user.setContactNumber(request.getContactNumber());
		user.setAddress(request.getAddress());
		user.setGender(request.getGender());
		user.setRole(User.Role.USER); // default role
		user.setActive(true);

		return userRepository.save(user);
	}

	@Override
	public LoginResponse loginUser(LoginRequest request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found!"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid password!");
		}

		if (!user.isActive()) {
			throw new RuntimeException("Account is deactivated!");
		}

		String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

		LoginResponse response = new LoginResponse();
		response.setToken(token);
		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole().name());
		response.setMessage("Login successful!");

		return response;
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with email: " + email));
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User updateUser(Long id, RegisterRequest request) {
		User user = getUserById(id);
		user.setName(request.getName());
		user.setContactNumber(request.getContactNumber());
		user.setAddress(request.getAddress());
		user.setGender(request.getGender());
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(Long id) {
		User user = getUserById(id);
		user.setActive(false); // soft delete
		userRepository.save(user);
	}

	@Override
	public boolean emailExists(String email) {
		return userRepository.existsByEmail(email);
	}
}
