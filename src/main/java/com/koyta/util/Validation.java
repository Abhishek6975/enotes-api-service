package com.koyta.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.bytecode.internal.bytebuddy.PrivateAccessorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.koyta.dto.CategoryDto;
import com.koyta.dto.TodoDto;
import com.koyta.dto.TodoDto.StatusDto;
import com.koyta.dto.UserRequest;
import com.koyta.entity.Role;
import com.koyta.entity.User;
import com.koyta.enums.TodoStatus;
import com.koyta.exception.ExistDataException;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.exception.ValidationException;
import com.koyta.repository.RoleRepository;
import com.koyta.repository.UserRepository;

@Component
public class Validation {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;

	public void categoryValidation(CategoryDto categoryDto) {

		Map<String, Object> error = new LinkedHashMap<>();

		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("Category Object/JSON Shouldn't be null or empty");
		} else {

			// validation name field
			if (ObjectUtils.isEmpty(categoryDto.getName())) {
				error.put("name", "Category field is Empty or Null");
			} else {

				if (categoryDto.getName().length() < 3) {
					error.put("name", "name length min 3");
				}
				if (categoryDto.getName().length() > 100) {
					error.put("name", "name length max 100");
				}
			}

			// Description Validation field
			if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
				error.put("description", "Description field is Empty or Null");
			} else {

				if (categoryDto.getDescription().length() < 10) {
					error.put("description", "Description length min 10");
				}
				if (categoryDto.getName().length() > 100) {
					error.put("description", "Description length max 100");
				}
			}

			// validation isActive Field
			if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				error.put("isActive", "isActive field is Empty or Null");
			} else {

				if (categoryDto.getIsActive() != Boolean.TRUE.booleanValue()
						&& categoryDto.getIsActive() != Boolean.FALSE.booleanValue()) {
					error.put("isActive", "invalid value isActive field");
				}
			}
		}

		if (!error.isEmpty()) {
			throw new ValidationException(error);
		}

	}

	public void todoValidation(TodoDto todoDto) throws Exception {

		StatusDto reqStatus = todoDto.getStatus();

		Boolean statusFound = false;

		TodoStatus[] status = TodoStatus.values();

		for (TodoStatus st : TodoStatus.values()) {

			if (st.getId().equals(reqStatus.getId())) {

				statusFound = true;

			}
		}

		if (!statusFound) {
			throw new ResourceNotFoundException("invalid Status");
		}
	}

	public void userValidation(UserRequest userDto) throws Exception {

		if (!StringUtils.hasText(userDto.getFirstName())) {
			throw new ResourceNotFoundException("first name is Invalid");
		}

		if (!StringUtils.hasText(userDto.getLastName())) {
			throw new ResourceNotFoundException("last name is Invalid");
		}

		if (!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(AppConstants.EMAIL_REGEX)) {
			throw new ResourceNotFoundException("email is Invalid");
		} else {
			Boolean existsEmail = userRepository.existsByEmail(userDto.getEmail());

			if (existsEmail) {

				throw new ExistDataException("Email already Exist");
			}
		}
		
		if (!StringUtils.hasText(userDto.getMobileNo()) || !userDto.getMobileNo().matches(AppConstants.MOBILENO_REGEX)) {
			throw new ResourceNotFoundException("Mobile No.is Invalid");
		}
		
		if (CollectionUtils.isEmpty(userDto.getRoles())) {

			throw new ResourceNotFoundException("role is Invalid");
		} else {

			List<Integer> rolesIds = roleRepository.findAll().stream().map(role -> role.getId()).toList();

			List<Integer> invalidReqRoleIds = userDto.getRoles().stream().map(role -> role.getId()).
					filter(roleId -> !rolesIds.contains(roleId)).toList();
			
			if (!CollectionUtils.isEmpty(invalidReqRoleIds)) {
				throw new ResourceNotFoundException("role is Invalid " + invalidReqRoleIds);
			}
		}
		
	}
}
