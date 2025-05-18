package com.koyta.service.Impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.koyta.dto.CategoryDto;
import com.koyta.dto.CategoryResponse;
import com.koyta.entity.Category;
import com.koyta.exception.ExistDataException;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.repository.CategoryRepository;
import com.koyta.service.CategoryService;
import com.koyta.util.Validation;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validation validation;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {

		// Validation Checking
		validation.categoryValidation(categoryDto);

		// Check Category exist or Not

		Boolean exist = categoryRepository.existsByName(categoryDto.getName().trim());

		if (exist) {

			// throw error

			throw new ExistDataException("Category already Exist");
		}

		Category category = modelMapper.map(categoryDto, Category.class);

		if (ObjectUtils.isEmpty(category.getId())) {

			category.setIsDeleted(false);
			// category.setCreatedBy(1);
			// category.setCreatedOn(new Date());
		} else {

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

		if (findById.isPresent()) {

			Category existCategory = findById.get();
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setIsDeleted(existCategory.getIsDeleted());

			// category.setUpdatedBy(1);
			// category.setUpdatedOn(new Date());

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
	public CategoryDto getCategoryById(Integer id) throws Exception {

		Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Found with Id " + id));

		if (!ObjectUtils.isEmpty(category)) {

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
