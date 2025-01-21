package com.koyta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.PswdResetRequest;
import com.koyta.service.HomeService;
import com.koyta.service.UserService;
import com.koyta.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {
	
	Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private HomeService homeService;

	@Autowired
	private UserService userService;

	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserAccount(@RequestParam("id") Integer id, @RequestParam("vc") String vc)
			throws Exception {

		log.info("HomeController : verifyUserAccount : Execution Start");
		Boolean verifyAccount = homeService.verifyAccount(id, vc);

		if (verifyAccount) {

			return CommonUtil.createBuildResponseMessage("Account verification success", HttpStatus.OK);
		}
		
		log.info("HomeController :verifyUserAccount : Execution End");
		return CommonUtil.createErrorResponseMessage("invalid Verification link", HttpStatus.BAD_REQUEST);

	}

	@PostMapping("/send-email-reset")
	public ResponseEntity<?> sendEmailForPasswordRest(@RequestParam("email") String email, HttpServletRequest request)
			throws Exception {

		userService.sendEmailPasswordReset(email, request);

		return CommonUtil.createBuildResponseMessage("Email sent successfully! Please check your inbox to proceed.", HttpStatus.OK);
	}

	@GetMapping("/verify-pswd-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam("id") Integer id, @RequestParam("vc") String vc) throws Exception {

		userService.verifyPswdRestLink(id, vc);

		return CommonUtil.createBuildResponseMessage("verification success", HttpStatus.OK);

	}

	@PostMapping("/reset-pswd")
	public ResponseEntity<?> restPassword(@RequestBody PswdResetRequest pswdResetRequest) throws Exception {

		userService.resetPassword(pswdResetRequest);

		return CommonUtil.createBuildResponseMessage("Password reset success", HttpStatus.OK);
		
	}

}
