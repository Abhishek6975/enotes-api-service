package com.koyta.service.Impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.FlashMap;

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

		if (ObjectUtils.isEmpty(category.getId())) {
			
			category.setIsDeleted(false);
			category.setCreatedBy(1);
			category.setCreatedOn(new Date());
		}else {
			
			updateCategory(category);
			
		}

		Category save = categoryRepository.save(category);

		if (ObjectUtils.isEmpty(save)) {

			return false;
		}

		return true;

	}

	private void updateCategory(Category category) {
		
		Optional<Category> findById = categoryRepository.findById(category.getId());
		
		if(findById.isPresent()) {
			
			Category existCategory = findById.get();
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setIsDeleted(existCategory.getIsDeleted());
			
			category.setUpdatedBy(1);
			category.setUpdatedOn(new Date());
			
		}
		
	}

	@Override
	public List<CategoryDto> getAllCategory() {

		List<Category> categories = categoryRepository.findByIsDeletedFalse();

		List<CategoryDto> categoryDtosList = categories.stream().map(cat -> modelMapper.map(cat, CategoryDto.class))
				.toList();

		return categoryDtosList;
	}

	@Override
	public List<CategoryResponse> getActiveCategory() {

		List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();

		List<CategoryResponse> categoryList = categories.stream()
				.map(cat -> modelMapper.map(cat, CategoryResponse.class)).toList();

		return categoryList;
	}

	@Override
	public CategoryDto getCategoryById(Integer id) {

		Optional<Category> findByCategory = categoryRepository.findByIdAndIsDeletedFalse(id);

		if (findByCategory.isPresent()) {

			Category category = findByCategory.get();

			return modelMapper.map(category, CategoryDto.class);
		}

		return null;

	}

	@Override
	public Boolean deleteCategoryById(Integer id) {

		Optional<Category> findByCategory = categoryRepository.findById(id);

		if (findByCategory.isPresent()) {

			Category category = findByCategory.get();
			category.setIsDeleted(true);
			categoryRepository.save(category);
			return true;

		}

		return false;

	}

}
