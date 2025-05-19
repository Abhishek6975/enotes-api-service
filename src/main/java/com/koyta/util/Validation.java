package com.koyta.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.koyta.dto.CategoryDto;
import com.koyta.dto.TodoDto;
import com.koyta.dto.TodoDto.StatusDto;
import com.koyta.dto.UserRequest;
import com.koyta.enums.TodoStatus;
import com.koyta.exception.ExistDataException;
import com.koyta.exception.InvalidInputException;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.exception.ValidationException;
import com.koyta.repository.RoleRepository;
import com.koyta.repository.UserRepository;

@Component
public class Validation {
	
	private RoleRepository roleRepository;

	private UserRepository userRepository;

	public Validation(RoleRepository roleRepository, UserRepository userRepository) {
		super();
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	public void categoryValidation(CategoryDto categoryDto) {

		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("Category Object/JSON shouldn't be null or empty");
		}

		Map<String, Object> errors = new LinkedHashMap<>();

		validateName(categoryDto.getName(), errors);
		validateDescription(categoryDto.getDescription(), errors);
		validateIsActive(categoryDto.getIsActive(), errors);

		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
	}

	private void validateName(String name, Map<String, Object> errors) {

		if (ObjectUtils.isEmpty(name)) {
			errors.put("name", "Category field is Empty or Null");
			return;
		}

		if (name.length() < 3) {
			errors.put("name", "Name length min 3");
		} else if (name.length() > 100) {
			errors.put("name", "Name length max 100");
		}
	}

	private void validateDescription(String description, Map<String, Object> errors) {

		if (ObjectUtils.isEmpty(description)) {
			errors.put("description", "Description field is Empty or Null");
			return;
		}

		if (description.length() < 10) {
			errors.put("description", "Description length min 10");
		} else if (description.length() > 100) {
			errors.put("description", "Description length max 100");
		}
	}

	private void validateIsActive(Boolean isActive, Map<String, Object> errors) {

		if (isActive == null) {
			errors.put("isActive", "isActive field is Empty or Null");
		}
	}

	public void todoValidation(TodoDto todoDto) throws ResourceNotFoundException {

		StatusDto reqStatus = todoDto.getStatus();

		boolean statusFound = false;

		for (TodoStatus st : TodoStatus.values()) {

			if (st.getId().equals(reqStatus.getId())) {

				statusFound = true;

			}
		}

		if (!statusFound) {
			throw new ResourceNotFoundException("invalid Status");
		}
	}

	public void userValidation(UserRequest userDto) throws InvalidInputException, ExistDataException {

		if (!StringUtils.hasText(userDto.getFirstName())) {
			throw new InvalidInputException("First name is invalid");
		}

		if (!StringUtils.hasText(userDto.getLastName())) {
			throw new InvalidInputException("Last name is invalid");
		}

		if (!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(AppConstants.EMAIL_REGEX)) {
			throw new ExistDataException("Email already exists");
		} else {
			boolean existsEmail = userRepository.existsByEmail(userDto.getEmail());

			if (existsEmail) {

				throw new ExistDataException("Email already Exist");
			}
		}
		
		if (!StringUtils.hasText(userDto.getMobileNo()) || !userDto.getMobileNo().matches(AppConstants.MOBILENO_REGEX)) {
			throw new InvalidInputException("Mobile number is invalid");
		}
		
		if (CollectionUtils.isEmpty(userDto.getRoles())) {

			throw new InvalidInputException("Role is missing");
		} else {

			List<Integer> rolesIds = roleRepository.findAll().stream().map(role -> role.getId()).toList();

			List<Integer> invalidReqRoleIds = userDto.getRoles().stream().map(role -> role.getId()).
					filter(roleId -> !rolesIds.contains(roleId)).toList();
			
			if (!CollectionUtils.isEmpty(invalidReqRoleIds)) {
				throw new InvalidInputException("Invalid role IDs: " + invalidReqRoleIds);
			}
		}
		
	}
}
