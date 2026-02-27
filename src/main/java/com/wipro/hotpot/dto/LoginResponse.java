package com.wipro.hotpot.dto;

public class LoginResponse {

	private String token;
	private String name;
	private String email;
	private String role;
	private String message;

	public LoginResponse() {
	}

	public LoginResponse(String token, String name, String email, String role, String message) {
		this.token = token;
		this.name = name;
		this.email = email;
		this.role = role;
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}

	public String getMessage() {
		return message;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
