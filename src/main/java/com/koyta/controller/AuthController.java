package com.koyta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.UserDto;
import com.koyta.service.UserService;
import com.koyta.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception {

		Boolean register = userService.register(userDto);

		if (register) {
			
			return CommonUtil.createBuildResponseMessage("Register Success", HttpStatus.CREATED);
		}

		return CommonUtil.createErrorResponseMessage("Failed Registration", HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
