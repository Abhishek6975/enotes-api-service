package com.koyta.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.PasswordChngRequest;
import com.koyta.dto.UserResponse;
import com.koyta.endpoint.UserEndpoint;
import com.koyta.entity.User;
import com.koyta.service.UserService;
import com.koyta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController implements UserEndpoint {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;

	@GetMapping("/profile")
	public ResponseEntity<?> getProfile() {

		User loggedInUser = CommonUtil.getLoggedInUser();

		UserResponse userResponse = modelMapper.map(loggedInUser, UserResponse.class);

		return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);

	}

	@PostMapping("/chng-pswd")
	public ResponseEntity<?> changePassword(PasswordChngRequest passwordChngRequest) throws Exception {

		userService.changePassword(passwordChngRequest);
		return CommonUtil.createBuildResponseMessage("Password Changed Success", HttpStatus.OK);
	}

}
