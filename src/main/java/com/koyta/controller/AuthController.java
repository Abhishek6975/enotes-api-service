package com.koyta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.LoginRequest;
import com.koyta.dto.LoginResponse;
import com.koyta.dto.UserRequest;
import com.koyta.endpoint.AuthEndpoint;
import com.koyta.exception.AuthenticationFailedException;
import com.koyta.exception.UserRegistrationException;
import com.koyta.service.AuthService;
import com.koyta.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AuthController implements AuthEndpoint {

	private AuthService authService;

	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}

	@Override
	public ResponseEntity<?> registerUser(UserRequest userDto, HttpServletRequest request) throws UserRegistrationException {
		
		log.info("AuthController : registerUser() : Execution Start");
		boolean register = authService.register(userDto, request);

		if (!register) {
			log.info("Error : ()", "Register failed");
			return CommonUtil.createErrorResponseMessage("Failed Registration", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("AuthController : registerUser() : Execution End");
		return CommonUtil.createBuildResponseMessage("Register Success", HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> login(LoginRequest loginRequest) throws AuthenticationFailedException  {

		LoginResponse loginResponse = authService.login(loginRequest);

		if (ObjectUtils.isEmpty(loginResponse)) {

			return CommonUtil.createErrorResponseMessage("invalid credentials", HttpStatus.BAD_REQUEST);
		}

		return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);

	}

}
