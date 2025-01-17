package com.koyta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PswdResetRequest {
	
	private Integer userId;
	
	private String newPassword; 
}
