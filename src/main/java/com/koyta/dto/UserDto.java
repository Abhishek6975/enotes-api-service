package com.koyta.dto;

import java.util.List;

import com.koyta.entity.Role;

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
public class UserDto {

	private Integer id;

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNo;
	
	private String password;

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

}
