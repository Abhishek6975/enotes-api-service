package com.koyta.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.koyta.dto.CategoryDto;
import com.koyta.exception.ValidationException;

@Component
public class Validation {

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
}
