package com.koyta.service.Impl;

import java.util.Date;
import java.util.List;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.koyta.dto.CategoryDto;
import com.koyta.dto.CategoryResponse;
import com.koyta.entity.Category;
import com.koyta.repository.CategoryRepository;
import com.koyta.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {

		/*
		 * Category category = new Category(); category.setName(categoryDto.getName());
		 * category.setDescription(categoryDto.getDescription());
		 * category.setIsActive(categoryDto.getIsActive());
		 */

		Category category = modelMapper.map(categoryDto, Category.class);

		category.setIsDeleted(false);
		category.setCreatedBy(1);
		category.setCreatedOn(new Date());

		Category save = categoryRepository.save(category);

		if (ObjectUtils.isEmpty(save)) {

			return false;
		}

		return true;

	}

	@Override
	public List<CategoryDto> getAllCategory() {

		List<Category> categories = categoryRepository.findAll();

		List<CategoryDto> categoryDtosList = categories.stream().map(cat -> modelMapper.map(cat, CategoryDto.class))
				.toList();

		return categoryDtosList;
	}

	@Override
	public List<CategoryResponse> getActiveCategory() {

		List<Category> categories = categoryRepository.findByIsActiveTrue();

		List<CategoryResponse> categoryList = categories.stream()
				.map(cat -> modelMapper.map(cat, CategoryResponse.class)).toList();

		return categoryList;
	}

}
