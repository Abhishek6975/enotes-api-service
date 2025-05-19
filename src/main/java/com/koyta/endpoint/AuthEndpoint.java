package com.koyta.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koyta.dto.LoginRequest;
import com.koyta.dto.UserRequest;
import com.koyta.exception.AuthenticationFailedException;
import com.koyta.exception.UserRegistrationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Authentication",description = "All the user Authentication API's")
@RequestMapping("/api/v1/auth")
public interface AuthEndpoint {

	@ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "Register Success"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
			@ApiResponse(responseCode = "400" , description = "Bad Request")
			})
	@Operation(summary = "User Register Endpoint",tags = {"Home"})
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto, HttpServletRequest request)
			throws UserRegistrationException;

	@Operation(summary = "User Login Endpoint",tags = {"Home"})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws AuthenticationFailedException ;
}
