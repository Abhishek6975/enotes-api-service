package com.koyta.service;

import java.util.List;

import com.koyta.dto.CategoryDto;
import com.koyta.dto.CategoryResponse;
import com.koyta.exception.ResourceNotFoundException;

public interface CategoryService {

	public Boolean saveCategory(CategoryDto categoryDto);

	public List<CategoryDto> getAllCategory();

	public List<CategoryResponse> getActiveCategory();

	public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException;

	public Boolean deleteCategoryById(Integer id);
}
