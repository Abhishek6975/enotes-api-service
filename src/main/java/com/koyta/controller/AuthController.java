package com.koyta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.LoginRequest;
import com.koyta.dto.LoginResponse;
import com.koyta.dto.UserRequest;
import com.koyta.service.AuthService;
import com.koyta.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto, HttpServletRequest request) throws Exception {

		String url = CommonUtil.getUrl(request);

		Boolean register = authService.register(userDto, url);

		if (register) {

			return CommonUtil.createBuildResponseMessage("Register Success", HttpStatus.CREATED);
		}

		return CommonUtil.createErrorResponseMessage("Failed Registration", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {

		LoginResponse loginResponse = authService.login(loginRequest);

		if (ObjectUtils.isEmpty(loginResponse)) {

			return CommonUtil.createErrorResponseMessage("invalid credentials", HttpStatus.BAD_REQUEST);
		}

		return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);

	}

}
