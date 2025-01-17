package com.koyta.dto;

import java.util.List;

import com.koyta.dto.TodoDto.StatusDto;

import ch.qos.logback.core.status.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

	private Integer id;

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNo;
	
	private StatusDto status;

	private List<RoleDto> roles;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class RoleDto {

		private Integer id;

		private String name;
	}
	
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class StatusDto {

		private Integer id;

		private Boolean isActive;

	}
	
	
}
