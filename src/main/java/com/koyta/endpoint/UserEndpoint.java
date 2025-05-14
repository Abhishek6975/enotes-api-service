package com.koyta.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koyta.dto.PasswordChngRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User",description = "Authetication User Operation API's")
@RequestMapping("/api/v1/user")
public interface UserEndpoint {

	@Operation(summary = "Get User Profile",description = "Get User Profile ")
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile();

	@Operation(summary = "Change User Account Password",description = "Change User Account Password")
	@PostMapping("/chng-pswd")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChngRequest passwordChngRequest) throws Exception;
	
}
