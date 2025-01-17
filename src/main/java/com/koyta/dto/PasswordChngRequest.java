package com.koyta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChngRequest {
	
	private String oldPassword;

	private String newPassword;

}
