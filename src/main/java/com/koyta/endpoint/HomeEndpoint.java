package com.koyta.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koyta.dto.PswdResetRequest;
import com.koyta.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Home",description = "All the Home Operations API's")
@RequestMapping("/api/v1/home")
public interface HomeEndpoint {

	@Operation(summary = "Verifiaction User Account",description = "User Account Verification after register account")
	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserAccount(@RequestParam("id") Integer id, @RequestParam("vc") String vc)
			throws ResourceNotFoundException;

	@Operation(summary = "Send Email for Password Rest",description = "User Can send Email for Password Reset")
	@PostMapping("/send-email-reset")
	public ResponseEntity<?> sendEmailForPasswordRest(@RequestParam("email") String email, HttpServletRequest request)
			throws ResourceNotFoundException;

	@Operation(summary = "Verification Password Link",description = "User verification password link")
	@GetMapping("/verify-pswd-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam("id") Integer id, @RequestParam("vc") String vc)
			throws ResourceNotFoundException;

	@Operation(summary = "Reset Password" , description = "User can Change Password")
	@PostMapping("/reset-pswd")
	public ResponseEntity<?> restPassword(@RequestBody PswdResetRequest pswdResetRequest) throws ResourceNotFoundException;

}
