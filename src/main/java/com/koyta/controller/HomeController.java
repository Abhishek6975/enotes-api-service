package com.koyta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.PswdResetRequest;
import com.koyta.endpoint.HomeEndpoint;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.service.HomeService;
import com.koyta.service.UserService;
import com.koyta.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController implements HomeEndpoint {
	
	Logger log = LoggerFactory.getLogger(HomeController.class);

	private HomeService homeService;

	private UserService userService;

	public HomeController(HomeService homeService, UserService userService) {
		super();
		this.homeService = homeService;
		this.userService = userService;
	}

	@Override
	public ResponseEntity<?> verifyUserAccount(Integer id, String vc)
			throws ResourceNotFoundException {

		log.info("HomeController : verifyUserAccount : Execution Start");
		boolean verifyAccount = homeService.verifyAccount(id, vc);

		if (verifyAccount) {

			return CommonUtil.createBuildResponseMessage("Account verification success", HttpStatus.OK);
		}
		
		log.info("HomeController :verifyUserAccount : Execution End");
		return CommonUtil.createErrorResponseMessage("invalid Verification link", HttpStatus.BAD_REQUEST);

	}
	
	@Override
	public ResponseEntity<?> sendEmailForPasswordRest(String email, HttpServletRequest request)
			throws ResourceNotFoundException {

		userService.sendEmailPasswordReset(email, request);

		return CommonUtil.createBuildResponseMessage("Email sent successfully! Please check your inbox to proceed.", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> verifyPasswordResetLink(Integer id, String vc) 
			throws ResourceNotFoundException {

		userService.verifyPswdRestLink(id, vc);

		return CommonUtil.createBuildResponseMessage("verification success", HttpStatus.OK);

	}
	
	@Override
	public ResponseEntity<?> restPassword(PswdResetRequest pswdResetRequest) 
			throws ResourceNotFoundException {

		userService.resetPassword(pswdResetRequest);

		return CommonUtil.createBuildResponseMessage("Password reset success", HttpStatus.OK);
		
	}

}
