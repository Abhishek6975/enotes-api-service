package com.koyta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.service.HomeService;
import com.koyta.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

	@Autowired
	private HomeService homeService;

	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserAccount(@RequestParam("id") Integer id, @RequestParam("vc") String vc)
			throws Exception {

		Boolean verifyAccount = homeService.verifyAccount(id, vc);

		if (verifyAccount) {
			
			return CommonUtil.createBuildResponseMessage("Account verification success", HttpStatus.OK);
		}
		return CommonUtil.createErrorResponseMessage("invalid Verification link", HttpStatus.BAD_REQUEST);

	}

}
