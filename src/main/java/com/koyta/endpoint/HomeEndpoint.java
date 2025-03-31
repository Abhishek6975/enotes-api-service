package com.koyta.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koyta.dto.PswdResetRequest;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/api/v1/home")
public interface HomeEndpoint {

	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserAccount(@RequestParam("id") Integer id, @RequestParam("vc") String vc)
			throws Exception;

	@PostMapping("/send-email-reset")
	public ResponseEntity<?> sendEmailForPasswordRest(@RequestParam("email") String email, HttpServletRequest request)
			throws Exception;

	@GetMapping("/verify-pswd-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam("id") Integer id, @RequestParam("vc") String vc)
			throws Exception;

	@PostMapping("/reset-pswd")
	public ResponseEntity<?> restPassword(@RequestBody PswdResetRequest pswdResetRequest) throws Exception;

}
